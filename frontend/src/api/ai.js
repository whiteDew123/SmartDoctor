import axios from 'axios'

const AI_CONFIG = {
  apiKey: import.meta.env.VITE_AI_API_KEY,
  url: import.meta.env.VITE_AI_API_URL || 'https://www.sophnet.com/api/open-apis/v1/chat/completions',
  model: import.meta.env.VITE_AI_MODEL || 'DeepSeek-V4-Flash',
  maxTokens: 8192
}

export function chatWithAI(messages) {
  return axios.post(
    AI_CONFIG.url,
    {
      model: AI_CONFIG.model,
      messages: messages,
      max_tokens: AI_CONFIG.maxTokens,
      stream: false
    },
    {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${AI_CONFIG.apiKey}`
      },
      timeout: 60000
    }
  )
}