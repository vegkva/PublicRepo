from flask import render_template, Flask
from flask_pymongo import PyMongo
from dotenv import load_dotenv
import os

load_dotenv()
app = Flask(__name__)
app.config["MONGO_URI"] = os.getenv("MONGO_URI")

database = PyMongo(app)
database.init_app(app)

@app.route('/')
def index():

    return render_template('index.html')

@app.route('/weatherBergen', methods=["GET"])
def get_weather_bergen():
    bergen_collection = database.db.Bergen
    all_data = bergen_collection.find()

    return render_template('table.html', data=all_data)



@app.route('/weatherOslo', methods=["GET"])
def get_weather_oslo():
    oslo_collection = database.db.Oslo
    all_data = oslo_collection.find()

    return render_template('table.html', data=all_data)

@app.route('/weatherKristiansand', methods=["GET"])
def get_weather_kristiansand():
    kristiansand_collection = database.db.Kristiansand
    all_data = kristiansand_collection.find()

    return render_template('table.html', data=all_data)

@app.route('/weatherTrondheim', methods=["GET"])
def get_weather_trondheim():
    trondheim_collection = database.db.Trondheim
    all_data = trondheim_collection.find()

    return render_template('table.html', data=all_data)

if __name__ == "__main__":
    app.run(debug=True)