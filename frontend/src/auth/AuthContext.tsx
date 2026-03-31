import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { api } from '../api/apiClient';

type AuthUser = {
  userId: number;
  name: string;
  email: string;
  role: string;
};

type AuthContextValue = {
  user: AuthUser | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  loading: boolean;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const existingToken = localStorage.getItem('accessToken');
    const existingUser = localStorage.getItem('authUser');
    if (existingToken && existingUser) {
      setToken(existingToken);
      setUser(JSON.parse(existingUser) as AuthUser);
    }
    setLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    const res = await api.post('/api/v1/auth/login', { email, password });
    const data = res.data as {
      accessToken: string;
      userId: number;
      name: string;
      email: string;
      role: string;
    };
    localStorage.setItem('accessToken', data.accessToken);
    const u: AuthUser = { userId: data.userId, name: data.name, email: data.email, role: data.role };
    localStorage.setItem('authUser', JSON.stringify(u));
    setUser(u);
    setToken(data.accessToken);
  };

  const register = async (name: string, email: string, password: string) => {
    const res = await api.post('/api/v1/auth/register', { name, email, password });
    const data = res.data as {
      accessToken: string;
      userId: number;
      name: string;
      email: string;
      role: string;
    };
    localStorage.setItem('accessToken', data.accessToken);
    const u: AuthUser = { userId: data.userId, name: data.name, email: data.email, role: data.role };
    localStorage.setItem('authUser', JSON.stringify(u));
    setUser(u);
    setToken(data.accessToken);
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('authUser');
    setUser(null);
    setToken(null);
  };

  const value = useMemo(
    () => ({ user, token, login, register, logout, loading }),
    [user, token, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

