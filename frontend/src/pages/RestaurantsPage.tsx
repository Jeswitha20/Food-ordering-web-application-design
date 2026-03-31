import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api/apiClient';

type Restaurant = {
  id: number;
  name: string;
  cuisine: string | null;
  address: string;
  rating: number | null;
  imageUrl: string | null;
};

export default function RestaurantsPage() {
  const [query, setQuery] = useState('');
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get('/api/v1/restaurants', { params: { q: query || undefined } });
      setRestaurants(res.data as Restaurant[]);
    } catch (e: any) {
      setError(e?.response?.data?.message ?? 'Failed to load restaurants');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const onSearch = () => void load();

  return (
    <div>
      <div style={{ display: 'flex', gap: 10, marginBottom: 16 }}>
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search for restaurants or cuisines..."
          style={{ flex: 1 }}
        />
        <button className="btn-primary" onClick={onSearch}>
          Search
        </button>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div style={{ color: 'crimson' }}>{error}</div>}

      <div
        className="grid"
        style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))' }}
      >
        {restaurants.map((r) => (
          <Link
            key={r.id}
            to={`/restaurant/${r.id}`}
            style={{
              textDecoration: 'none',
              color: '#111',
              cursor: 'pointer'
            }}
          >
            <div className="restaurant-card">
              <div className="restaurant-card__image-wrap">
                <img
                  className="restaurant-card__image"
                  src={
                    r.imageUrl ||
                    'https://images.pexels.com/photos/1640774/pexels-photo-1640774.jpeg?auto=compress&cs=tinysrgb&w=800'
                  }
                  alt={r.name}
                />
              </div>
              <div className="restaurant-card__title">{r.name}</div>
              <div className="restaurant-card__meta">
                {(r.cuisine ?? 'Cuisine') as any}
                {r.rating ? ` • ★ ${r.rating.toFixed(1)}` : ''}
              </div>
              <div className="restaurant-card__address">{r.address}</div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

