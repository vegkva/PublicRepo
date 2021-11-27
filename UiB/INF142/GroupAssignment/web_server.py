from socket import socket, AF_INET, SOCK_DGRAM
from http.server import BaseHTTPRequestHandler, HTTPServer
from dotenv import load_dotenv
import time, os
import json

hostname = "localhost"
httpPort = 8080

storageHostname = "localhost"
storagePort = 5555

class WebServer(BaseHTTPRequestHandler):
    """ Requests data from storage_server
    """
    def requestData(self):
        try:
            sock = socket(AF_INET, SOCK_DGRAM)
            sock.sendto("GET".encode(), (os.getenv("HOST"), int(os.getenv("PORT"))))
            data, address = sock.recvfrom(2000*80)
            return json.loads(data.decode())
        except:
            print("Error requesting data")
            return ["Error,Or,Timeout,"]
            pass

    """ Converts data recieved to a HTML table
    """
    def getTableAsString(self):
        data = self.requestData()
        print(data[0])
        #data = sampleData.split("\n")
        table = "<table><tr>"
        table += "<th>Time</th>"
        table += "<th>Place</th>"
        table += "<th>Temperature (C°)</th>"
        table += "<th>Rain (mm)</th>"
        table += "</tr>"
        for line in data:
            d = line.split(',')
            if (len(d) < 4): # Make sure we have enough fields
                continue
            table += "<tr>"
            for i in range(0,4):
                table += "<td>%s</td>"%d[i]
            table += "</tr>"
        table += "</table>"
        return table

    """ Responds to any GET request from browsers.
    """
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(b"<html lang=\"no\"><head><meta charset=\"utf-8\">")
        # Ugly code that gives the html table a nice border
        self.wfile.write(b"<style>table,th,td {border: 1px solid black;border-collapse:collapse;}"
                        b"th,td {padding:5px;text-align:center;}"
                        b"</style>")
        self.wfile.write(b"<title>Weather Station Database</title></head>")
        self.wfile.write(b"<body>")
        self.wfile.write(self.getTableAsString().encode())
        self.wfile.write(b"</body></html>")

if __name__ == "__main__":
    load_dotenv()
    webServer = HTTPServer((os.getenv("HTTPHOST"), int(os.getenv("HTTPPORT"))), WebServer)
    print(f"Server started http://%s:%i" % (os.getenv("HTTPHOST"), int(os.getenv("HTTPPORT"))))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")