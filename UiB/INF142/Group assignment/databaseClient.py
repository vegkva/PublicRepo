import pymongo
from pymongo import MongoClient


class DatabaseClient:
    """Connecting a user to the database
    with read and write permission.
    """

    def __init__(self, username, password, clusterName, database):
        self._username = username
        self._password = password
        self._clusterName = clusterName
        self._client = MongoClient('mongodb+srv://' + self._username + ':' + self._password + '@' + self._clusterName +
                                   f'.m4bix.mongodb.net/{database}?retryWrites=true&w=majority')
        self._database = self._client[database]

    def add_data(self, station: str, weather_data: str):
        """Adding data from a weather station to a collection (table).
        :param station: The source of the weather data
        :param weather_data: The data to be added
        :return: None
        """
        if weather_data != "":
            try:
                date, city, temp, peri = weather_data.split(",")
                data = {"Date": date, "City": city.strip(), "Temperature": temp.strip(), "Precipitation": peri.strip()}
                collection = self._database[station]
                collection.insert_one(data)
            except:
                print("Data incorrectly formatted")
            else:
                print("Data succesfully added")

    def retrieve_data(self, station):
        """Retrieves all the data from the selected station
        :param station: Name of collection in the database
        :return: A list of weather data
        """
        list_of_data = []
        if station in self._database.list_collection_names():
            results = self._database[station].find({})

            for result in results:
                parsed_result = result["Date"] + ", " + result["City"] + ", " + str(result["Temperature"]) \
                                + ", " + str(result["Precipitation"])
                list_of_data.append(parsed_result)
            return list_of_data

        else:
            print(f"There is no data for {station}")

    def clear_collection(self, station, are_you_sure=False):
        """
        The purpose of this method is to effortlessly clear a collection.
        To be used when testing the database.
        :param station: The database
        :return: None
        """
        if station in self._database.list_collection_names():
            if are_you_sure:
                collection = self._database[station]
                collection.delete_many({})
                print(f"Collections in {station} deleted")
        else:
            print(f"There is no data for {station}")

