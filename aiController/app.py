import os
from dotenv import load_dotenv
from flask import Flask
from openai import OpenAI

client = OpenAI(api_key=os.getenv("OPEN_AI_KEY"))

load_dotenv()

app = Flask(__name__)


@app.route("/")
def hello():
    return "Remnant AI server is running."
