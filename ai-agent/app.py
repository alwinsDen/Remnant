import os
from dotenv import load_dotenv
from flask import Flask, request, jsonify, Response
from kubernetes.stream import stream
from openai import OpenAI
import chromadb
from concurrent.futures import ThreadPoolExecutor
from prompt_engineeing.creators.components import generate_system_prompt, embedding_query_content
import uuid

client = OpenAI(api_key=os.getenv("OPEN_AI_KEY"))

load_dotenv()

chroma_client = chromadb.PersistentClient(path="db")
collection = chroma_client.get_or_create_collection(name="user_prompt_history")

app = Flask(__name__)

executor = ThreadPoolExecutor(max_workers=2)

with open("systemPrompts/dbt_agent_promtp.txt", "r") as f:
    system_prompt = f.read()

def add_to_collection_async(user_query,documents, metadatas, ids):
    collection.add(documents=embedding_query_content(
        agent_response=documents,
        user_query=user_query
    ), metadatas=metadatas, ids=ids)

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
        generated_content = []
        def generate():
            response = client.chat.completions.create(
                model="gpt-4o",
                messages=[
                    {
                        "role": "system",
                        "content": generate_system_prompt(system_prompt=system_prompt, past_conversation="")
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
                    generated_content.append(chunk.choices[0].delta.content)
                    yield chunk.choices[0].delta.content.encode("utf-8")
        server_response = Response(generate(), content_type="text/plain")
        executor.submit(
            add_to_collection_async,
            user_query,
            (server_response.data).decode("utf-8"),
            {"source": "test1"},
            str(uuid.uuid4())
        )
        return server_response
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/get_chroma_queries", methods=["GET"])
def get_chome_queries():
    print(collection.count())
    return Response("Change the color.")