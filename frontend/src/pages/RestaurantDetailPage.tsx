import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { api } from '../api/apiClient';
import { useCart } from '../cart/CartContext';

type MenuItem = {
  id: number;
  name: string;
  description: string | null;
  price: number;
  available: boolean;
};

type RestaurantDetail = {
  restaurant: {
    id: number;
    name: string;
    cuisine: string | null;
    address: string;
    rating: number | null;
    imageUrl: string | null;
  };
  menu: MenuItem[];
};

export default function RestaurantDetailPage() {
  const { id } = useParams();
  const restaurantId = Number(id);
  const navigate = useNavigate();
  const { addItem } = useCart();

  const [detail, setDetail] = useState<RestaurantDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [actionError, setActionError] = useState<string | null>(null);

  const [menuQ, setMenuQ] = useState('');
  const [quantities, setQuantities] = useState<Record<number, number>>({});

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get(`/api/v1/restaurants/${restaurantId}`, {
        params: { menuQ: menuQ || undefined }
      });
      setDetail(res.data as RestaurantDetail);
    } catch (e: any) {
      setError(e?.response?.data?.message ?? 'Failed to load menu');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [restaurantId]);

  const visibleMenu = useMemo(() => detail?.menu ?? [], [detail]);

  return (
    <div>
      <button className="btn-ghost" onClick={() => window.history.back()} style={{ marginBottom: 14 }}>
        ← Back
      </button>

      {detail && (
        <div className="card card--soft" style={{ marginBottom: 16, padding: 0, overflow: 'hidden' }}>
          <div style={{ height: 180, overflow: 'hidden' }}>
            <img
              src={
                detail.restaurant.imageUrl ||
                'https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg?auto=compress&cs=tinysrgb&w=800'
              }
              alt={detail.restaurant.name}
              style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
            />
          </div>
          <div style={{ padding: 14 }}>
            <div style={{ fontSize: 22, fontWeight: 900 }}>{detail.restaurant.name}</div>
            <div style={{ color: '#4b5563', marginTop: 4, fontSize: 14 }}>
              {detail.restaurant.cuisine ?? ''}{' '}
              {detail.restaurant.rating ? `• ★ ${detail.restaurant.rating.toFixed(1)}` : ''}
            </div>
            <div style={{ color: '#9ca3af', marginTop: 4, fontSize: 13 }}>{detail.restaurant.address}</div>
          </div>
        </div>
      )}

      <div style={{ display: 'flex', gap: 12, marginBottom: 16 }}>
        <input value={menuQ} onChange={(e) => setMenuQ(e.target.value)} placeholder="Search menu..." />
        <button className="btn-primary" onClick={() => void load()}>
          Search
        </button>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div style={{ color: 'crimson' }}>{error}</div>}
      {actionError && <div style={{ color: 'crimson', marginBottom: 10 }}>{actionError}</div>}

      {visibleMenu.length === 0 && !loading && <div>No menu items found.</div>}

      <div
        className="grid"
        style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))' }}
      >
        {visibleMenu.map((m) => {
          const qty = quantities[m.id] ?? 1;
          return (
            <div key={m.id} className="card card--soft">
              <div style={{ fontWeight: 900 }}>{m.name}</div>
              <div style={{ color: '#6b7280', marginTop: 6, fontSize: 13 }}>{m.description}</div>
              <div style={{ marginTop: 10, fontWeight: 800 }}>₹{m.price.toFixed(2)}</div>

              <div style={{ display: 'flex', gap: 10, alignItems: 'center', marginTop: 12 }}>
                <input
                  type="number"
                  min={1}
                  value={qty}
                  onChange={(e) => setQuantities((prev) => ({ ...prev, [m.id]: Number(e.target.value) }))}
                  style={{ width: 90 }}
                />
                <button
                  className="btn-primary"
                  disabled={!m.available}
                  onClick={async () => {
                    setActionError(null);
                    try {
                      await addItem(m.id, qty);
                      navigate('/cart');
                    } catch (e: any) {
                      setActionError(
                        e?.response?.data?.message ??
                        'Unable to add item to cart. If your cart has items from another restaurant, clear it first.'
                      );
                    }
                  }}
                >
                  Add to Cart
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

