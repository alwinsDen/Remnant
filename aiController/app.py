
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
                        "text": "you have simple task to do. you have a query coming in. based on the content, determine whether\n1. it is a general query completely like an expression of gesture of greeting, a bunch of non sensical words etc.\n2. whether it is a query questioning / asking things from you given codebase. this is something that is technical, but this is NOT a query asking you to implement something. its just purely questioning etc.\n3. now this is the query that deals with execution statements. this needs to be technical, CAN be a question BUT needs to have something that points towards executing changes to codebase.\nreturn the output in JSON format with contains two key value pair.\nfirst one is- \"query\": followed by the users query,\nsecond is - \"q_type\": which is dependent on the above parameters, it can be \"greeting\",\"ask_codebase\" or \"execute\".\nexample:\n- `\"query\": \"Hello there!\"`, `\"q_type\": \"greeting\"`\n- `\"query\": \"What is the function of API X in the codebase?\"`, `\"q_type\": \"ask_codebase\"`\n- `\"query\": \"Can you refactor function Y to improve readability?\"`, `\"q_type\": \"execute\"`\n\nIMPORTANT:\n1. If the query is a mixture of questions and executions and gibberish, prioritise \"execution\" ALWAYS.\n2. If its mixture of questions & gibberish, prioritise \"greeting\" always.\n\nEnsure that the classification is unambiguous and categorizes each query by its intent clearly, matching the defined categories strictly."
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
        temperature=1,
        max_tokens=2048,
        top_p=1,
        frequency_penalty=0,
        presence_penalty=0
    )
    print(response.choices[0].message.content)
    return response.choices[0].message.content, 200
