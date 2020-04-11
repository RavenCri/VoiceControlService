import socket
import argparse
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--instruct", required=True,help="please input the instruct")
args = vars(ap.parse_args())
#创建一个客户端的socket对象
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(("192.168.1.100", 8080))
client.send(args['instruct'].encode("utf-8"))