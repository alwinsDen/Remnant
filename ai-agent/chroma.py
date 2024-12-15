import chromadb

chroma_client = chromadb.PersistentClient(path="db")
collection = chroma_client.get_or_create_collection(name="user_prompt_history")