from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello():
    return "Remnant AI server is running."