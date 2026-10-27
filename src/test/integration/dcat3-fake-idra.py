from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse, parse_qs
import json
state={'count':1001,'with_distribution':True}
class FixtureApi(BaseHTTPRequestHandler):
    def log_message(self,*args): pass
    def do_GET(self):
        request=urlparse(self.path);query=parse_qs(request.query)
        if request.path=='/control':
            state['count']=int(query.get('count',[state['count']])[0])
            state['with_distribution']=query.get('distribution',['true'])[0]=='true'
            data=state
        elif request.path.endswith('/datasets'):
            start=int(query.get('start',['0'])[0]);rows=int(query.get('rows',['1000'])[0]);data=[]
            for i in range(start,min(start+rows,state['count'])):
                data.append({'id':f'plan39-fixture-{i}','title':{'value':f'Budget {i}'},'description':{'value':'Fixture'},'previousVersion':['https://example.org/v1'],
                    'inSeries':[{'resourceUri':'https://example.org/series','title':[{'title':'Budgets','language':'en'}]}],
                    'distributions':[{'id':f'plan39-dist-{i}','title':{'value':'CSV'},'accessUrl':{'value':'https://example.org/data'}}] if state['with_distribution'] else []})
            data={'results':data}
        else:data={'id':987654,'name':'Plan 39 isolated fixture','host':'https://example.org','homepage':'https://example.org','description':'Synthetic catalogue','publisherName':'Fixture publisher'}
        body=json.dumps(data).encode();self.send_response(200);self.send_header('Content-Type','application/json');self.send_header('Content-Length',str(len(body)));self.end_headers();self.wfile.write(body)
HTTPServer(('127.0.0.1',18080),FixtureApi).serve_forever()
