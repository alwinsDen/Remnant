def generate_system_prompt(system_prompt, past_conversation):
    return f"""
        Here main system prompt is inside <system_prompt>. The past conversation context inside <past_conversation>. Respond taking into context <past_conversation> do NOTE <past_conversation> can also be empty.
        <system_prompt>
            {system_prompt}
        </system_prompt>
        <past_conversation>
             {past_conversation}   
        </past_conversation>
    """

def embedding_query_content(user_query, agent_response):
    return f"""
        <user_query>
        {user_query}
        </user_query>
        <ai_agent_response>
        {agent_response}
        </ai_agent_response>
    """