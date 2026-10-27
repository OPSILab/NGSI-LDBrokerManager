$ErrorActionPreference='Stop'
$taskBroker='http://localhost:1026/ngsi-ld/v1/'
$taskManager='http://localhost:18082/'
$taskHeaders=@{Link='<http://host.docker.internal:18082/dcat3-context.jsonld>; rel="http://www.w3.org/ns/json-ld#context"; type="application/ld+json"'}
$taskConfig='{"catalogueId":"987654","contextBrokerUrl":"http://localhost:1026"}'
function Read-TaskOwned {
  $taskResult=@(); $taskOffset=0
  do {
    $taskPage=(Invoke-WebRequest -Uri ($taskBroker+'entities?q='+[Uri]::EscapeDataString('idraCatalogueId=="987654"')+'&limit=1000&offset='+$taskOffset) -Headers $taskHeaders).Content | ConvertFrom-Json -AsHashtable -NoEnumerate
    $taskResult+=@($taskPage);$taskOffset+=@($taskPage).Count
  } while (@($taskPage).Count -eq 1000)
  return $taskResult
}
$taskExisting=@(Read-TaskOwned)
if($taskExisting.Count -gt 0){throw 'Fixture owner is already present; refusing to overwrite it'}
$taskStarted=$false
try {
  Invoke-RestMethod -Uri 'http://localhost:18080/control?count=1001&distribution=true' | Out-Null
  $taskStarted=$true
  $taskFirst=(Invoke-WebRequest -Method Post -Uri ($taskManager+'startProcess') -ContentType 'application/json' -Body $taskConfig).StatusCode
  $taskInitial=@(Read-TaskOwned)
  $taskDatasets=@($taskInitial | Where-Object { $_.type -eq 'Dataset' })
  if($taskDatasets.Count -ne 1001){throw "Only $($taskDatasets.Count) datasets after sync"}
  $taskRepeat=(Invoke-WebRequest -Method Post -Uri ($taskManager+'startProcess') -ContentType 'application/json' -Body $taskConfig).StatusCode
  $taskRepeated=@(Read-TaskOwned)
  if($taskRepeated.Count -ne $taskInitial.Count){throw 'Repeated sync duplicated entities'}
  Invoke-RestMethod -Uri 'http://localhost:18080/control?count=1&distribution=false' | Out-Null
  $taskReduced=(Invoke-WebRequest -Method Post -Uri ($taskManager+'startProcess') -ContentType 'application/json' -Body $taskConfig).StatusCode
  $taskFinal=@(Read-TaskOwned)
  if(@($taskFinal | Where-Object {$_.type -eq 'Dataset'}).Count -ne 1){throw 'Stale datasets remain'}
  if(@($taskFinal | Where-Object {$_.type -eq 'DistributionDCAT-AP'}).Count -ne 0){throw 'Stale distributions remain'}
  [pscustomobject]@{firstStatus=$taskFirst;repeatStatus=$taskRepeat;reduceStatus=$taskReduced;datasets=1001;initialEntities=$taskInitial.Count;remainingEntities=$taskFinal.Count;idempotence='pass';staleCleanup='pass'} | ConvertTo-Json
} finally {
  if($taskStarted){Invoke-WebRequest -Method Post -Uri ($taskManager+'deleteCatalogue') -ContentType 'application/json' -Body $taskConfig | Select-Object StatusCode}
}
if(@(Read-TaskOwned).Count -ne 0){throw 'Fixture entities remain after delete'}
