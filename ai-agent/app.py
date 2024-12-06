
import os
from dotenv import load_dotenv
from flask import Flask, request
from openai import OpenAI

client = OpenAI(api_key=os.getenv("OPEN_AI_KEY"))

load_dotenv()

app = Flask(__name__)

@app.route("/", methods=["POST"])
def handle_query():
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {
                "role": "system",
                "content": [
                    {
                        "type": "text",
                        "text": "act lik an CBT counsellor. you are a counsellor who is helping a client who is having a hard time. you need to help them with their problems. you need to help them understand their problems and help them find solutions to their problems."
                    }
                ]
            },
            {
                "role": "user",
                "content": request.json.get("query"),
            },
        ],
        response_format={
            "type": "json_object"
        },
        temperature=0.2,
        max_tokens=2048,
        top_p=1,
        frequency_penalty=0,
        presence_penalty=0
    )
    print(response.choices[0].message.content)
    return response.choices[0].message.content, 200
