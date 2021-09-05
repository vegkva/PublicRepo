import socket
from _thread import *
import pickle
import time
import json

server = "localhost"
port = 5555

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    s.bind((server, port))
except socket.error as e:
    str(e)

s.listen(2)
print("Waiting for connection, Server Started")


def send_json(dictdata):
    dictJson = json.dumps(dictdata)
    return dictJson


dictdata1 = {
        "eatBool": False,
        "gameOverBool": False,
        "headX": 0,
        "headY": 0,
        "tailX": -700,
        "tailY": -700,
        "foodX": -700,
        "foodY": -700,
        "score": 0
    }

dictdata2 = {
        "eatBool": False,
        "gameOverBool": False,
        "headX": 100,
        "headY": 0,
        "tailX": -700,
        "tailY": -700,
        "foodX": -700,
        "foodY": -700,
        "score": 0
    }

pos = [dictdata1, dictdata2]

def threaded_client(conn, player):
    conn.send(send_json(pos[player]).encode())
    while True:
        try:
            data = json.loads(conn.recv(2048).decode('utf-8'))
            pos[player] = data
            if not data:
                print("Disconnected")
                break
            else:
                if player == 1:
                    reply = pos[0]
                else:
                    reply = pos[1]
            conn.sendall(send_json(reply).encode())
        except:
            break

    print("Lost connection")
    conn.close()


currentPlayer = 0
while True:
    conn, addr = s.accept()
    print(f"Connected to {addr}")

    start_new_thread(threaded_client, (conn, currentPlayer))

    currentPlayer += 1