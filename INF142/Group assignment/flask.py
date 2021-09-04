from flask import Flask
import FMI

app = Flask(__name__)

@app.route('/')
def side():
  return FMI.main_menu_gui(self)

if __name__ == '__main__':
  app.run(host='0.0.0.0', port='5555', debug=True)
