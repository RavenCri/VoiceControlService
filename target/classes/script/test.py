import serial #导入模块
import time
import argparse
try:
  ap = argparse.ArgumentParser()
  ap.add_argument("-p", "--portx", required=True,help="this param use set listen the port")
  ap.add_argument("-b", "--bps", required=True,help="this param use set bps")
  ap.add_argument("-m", "--msg", required=True,help="this param use set msg")
  args = vars(ap.parse_args())
  msg = args['msg']
  
  portx = args['portx']
 
  bps = int(args['bps'])
 
  #超时设置,None：永远等待操作，0为立即返回请求结果，其他值为等待超时时间(单位为秒）
  timex=0.5
  # 打开串口，并得到串口对象
  sr=serial.Serial(portx,bps,timeout=timex)
  time.sleep(1)
  result=sr.write(msg.encode("utf8"))
  sr.flush()
  #print("写总字节数:",result)
  print(str(sr.readline().decode("utf8")))
  sr.close()

except Exception as e:
    print("---异常---：",e)