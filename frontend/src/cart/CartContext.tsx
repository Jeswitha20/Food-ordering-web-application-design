import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { api } from '../api/apiClient';

type CartItem = {
  id: number;
  menuItemId: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  lineTotal: number;
};

type Cart = {
  cartId: number | null;
  restaurantId: number | null;
  items: CartItem[];
  totalAmount: number;
};

type CartContextValue = {
  cart: Cart | null;
  loading: boolean;
  refresh: () => Promise<void>;
  addItem: (menuItemId: number, quantity: number) => Promise<void>;
  updateItem: (menuItemId: number, quantity: number) => Promise<void>;
  removeItem: (menuItemId: number) => Promise<void>;
  clearLocal: () => void;
};

const CartContext = createContext<CartContextValue | undefined>(undefined);

function emptyCart(): Cart {
  return { cartId: null, restaurantId: null, items: [], totalAmount: 0 };
}

export function CartProvider({ children }: { children: React.ReactNode }) {
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);

  const refresh = async () => {
    setLoading(true);
    try {
      const res = await api.get('/api/v1/cart');
      setCart(res.data as Cart);
    } catch (e) {
      setCart(emptyCart());
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      void refresh();
    } else {
      setCart(emptyCart());
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const addItem = async (menuItemId: number, quantity: number) => {
    const res = await api.post('/api/v1/cart/items', { menuItemId, quantity });
    setCart(res.data as Cart);
  };

  const updateItem = async (menuItemId: number, quantity: number) => {
    const res = await api.patch(`/api/v1/cart/items/${menuItemId}`, { quantity });
    setCart(res.data as Cart);
  };

  const removeItem = async (menuItemId: number) => {
    const res = await api.delete(`/api/v1/cart/items/${menuItemId}`);
    setCart(res.data as Cart);
  };

  const clearLocal = () => {
    setCart(emptyCart());
  };

  const value = useMemo(
    () => ({ cart, loading, refresh, addItem, updateItem, removeItem, clearLocal }),
    [cart, loading]
  );

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used within CartProvider');
  return ctx;
}

