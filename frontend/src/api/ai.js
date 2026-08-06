import axios from 'axios'

const AI_CONFIG = {
  apiKey: 'OmQiPbQo4osbVNFNODwLX3gmM7SEwC3t2-DhH79ZbL8fgWIIxFtfaL-jbqdgHDZDERo5lb4zAk1TPP_PSFc4dA',
  url: 'https://www.sophnet.com/api/open-apis/v1/chat/completions',
  model: 'DeepSeek-V4-Flash',
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