import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

type OrderItem = {
  menuItemId: number;
  name: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
};

type Order = {
  id: number;
  restaurantId: number;
  totalAmount: number;
  status: string;
  createdAt: string;
  emailSent: boolean;
  items: OrderItem[];
};

export default function MyOrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [cancellingOrderId, setCancellingOrderId] = useState<number | null>(null);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await api.get('/api/v1/orders/me');
        setOrders(res.data as Order[]);
      } catch (e: any) {
        setError(e?.response?.data?.message ?? 'Failed to load orders');
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, []);

  const cancelOrder = async (orderId: number) => {
    setCancellingOrderId(orderId);
    setError(null);
    try {
      // Mocked checkout: cancellation only updates order status + sends cancellation email.
      await api.post(`/api/v1/orders/${orderId}/cancel`, {});
      const res = await api.get('/api/v1/orders/me');
      setOrders(res.data as Order[]);
    } catch (e: any) {
      setError(e?.response?.data?.message ?? 'Failed to cancel order');
    } finally {
      setCancellingOrderId(null);
    }
  };

  return (
    <div>
      <h2 className="page-title">My Orders</h2>

      {loading && <div>Loading orders...</div>}
      {error && <div style={{ color: 'crimson' }}>{error}</div>}

      {!loading && orders.length === 0 && <div>No orders yet.</div>}

      <div className="grid" style={{ marginTop: 14 }}>
        {orders.map((o) => (
          <div key={o.id} className="card card--soft">
            <div style={{ display: 'flex', gap: 12, alignItems: 'baseline' }}>
              <div style={{ fontWeight: 900 }}>Order #{o.id}</div>
              <span
                className={
                  o.status === 'CONFIRMED'
                    ? 'pill pill--success'
                    : o.status === 'PLACED'
                      ? 'pill pill--warning'
                      : 'pill pill--muted'
                }
              >
                {o.status}
              </span>
            </div>
            <div style={{ color: '#6b7280', marginTop: 6, fontSize: 13 }}>
              Placed at: {new Date(o.createdAt).toLocaleString()} • Email: {o.emailSent ? 'Sent' : 'Not sent'}
            </div>
            <div style={{ marginTop: 10, fontWeight: 900 }}>Total: ₹{o.totalAmount.toFixed(2)}</div>

            {(o.status === 'PLACED' || o.status === 'CONFIRMED') && (
              <div style={{ marginTop: 10 }}>
                <button
                  onClick={() => void cancelOrder(o.id)}
                  disabled={cancellingOrderId === o.id}
                  style={{ background: '#ef4444' }}
                >
                  {cancellingOrderId === o.id ? 'Cancelling...' : 'Cancel Order'}
                </button>
              </div>
            )}

            <div style={{ marginTop: 10 }}>
              {o.items.map((it) => (
                <div key={it.menuItemId} style={{ display: 'flex', justifyContent: 'space-between', marginTop: 6 }}>
                  <div>
                    {it.name} (x{it.quantity})
                  </div>
                  <div style={{ fontWeight: 800 }}>₹{it.lineTotal.toFixed(2)}</div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

