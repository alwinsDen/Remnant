def generate_system_prompt(system_prompt, past_conversation):
    return f"""
        Here main system prompt is inside <system_prompt>, it additionally contains <agentic_system_prompt> which is the main system prompt, <IMPORTANT_INSTRUCTIONS> contains addional instructions. The past conversation context is inside <past_conversation>. Respond taking into context <past_conversation> do NOTE <past_conversation> can also be empty.
        The <user_query> was asked by user and the immediate AI response (YOUR OWN PAST RESPONSE) is inside <ai_agent_response>. All the conversations are in perfect order of how they were asked.
        <system_prompt> {system_prompt} </system_prompt>
        <past_conversation> {past_conversation} </past_conversation>
    """

def embedding_query_content(user_query, agent_response):
    return f"""<user_query> {user_query} </user_query>
        <ai_agent_response> {agent_response} </ai_agent_response>
    """