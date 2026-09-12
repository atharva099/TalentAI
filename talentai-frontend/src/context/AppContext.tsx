import { createContext, type ReactNode } from 'react'

type AppContextValue = Record<string, never>

const AppContext = createContext<AppContextValue>({})

type AppProviderProps = {
  children: ReactNode
}

export function AppProvider({ children }: AppProviderProps) {
  return <AppContext.Provider value={{}}>{children}</AppContext.Provider>
}
