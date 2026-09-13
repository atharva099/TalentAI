export const AUTH_TOKEN_STORAGE_KEY = 'talentai_access_token'

export function getAccessToken() {
  return sessionStorage.getItem(AUTH_TOKEN_STORAGE_KEY)
}

export function setAccessToken(accessToken: string) {
  sessionStorage.setItem(AUTH_TOKEN_STORAGE_KEY, accessToken)
}

export function clearAccessToken() {
  sessionStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
}
