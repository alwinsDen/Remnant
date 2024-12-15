from chroma import collection

def timestamp_query_sorter_from_source(source_id):
    get_past_conversation_history = collection.get(
                where={"source_id": source_id}
            )
    sorted_vls = sorted(
                zip(get_past_conversation_history["documents"], get_past_conversation_history["metadatas"]),
                key=lambda x: x[1]["timestamp"]
            )
    return sorted_vls