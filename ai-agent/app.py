import os
from dotenv import load_dotenv
from flask import Flask, request, jsonify, Response
from kubernetes.stream import stream
from openai import OpenAI

client = OpenAI(api_key=os.getenv("OPEN_AI_KEY"))

load_dotenv()

app = Flask(__name__)


with open("systemPrompts/dbt_agent_promtp.txt", "r") as f:
    system_prompt = f.read()

@app.route("/")
def is_alive():
    return "The server is alive and pinging at 5000", 200

@app.route("/cbt_query", methods=["POST"])
def handle_query():
    try:
        user_query = request.json.get("query")

        # Check for a valid query
        if not user_query:
            return jsonify({"error": "Missing 'query' in the request body"}), 400

        def generate():
            response = client.chat.completions.create(
                model="gpt-4o",
                messages=[
                    {
                        "role": "system",
                        "content": system_prompt
                    },
                    {
                        "role": "user",
                        "content": user_query,
                    },
                ],
                temperature=0.2,
                stream=True
            )
            for chunk in response:
                if chunk.choices[0].delta.content:
                    yield chunk.choices[0].delta.content.encode("utf-8")

        return Response(generate(), content_type="text/plain")
    except Exception as e:
        return jsonify({"error": str(e)}), 500
