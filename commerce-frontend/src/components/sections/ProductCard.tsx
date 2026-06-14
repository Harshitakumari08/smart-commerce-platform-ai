import { Link } from 'react-router-dom';
import type { Product } from '../../services/productService';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  // Safe default values
  const price = product.price || 0;
  const rating = product.rating || 0;
  const brand = product.brand || 'Generic';
  const category = product.category || 'Uncategorized';
  const imageUrl = product.imageUrl || 'https://images.unsplash.com/photo-1531403009284-440f080d1e12?q=80&w=600&auto=format&fit=crop';

  return (
    <article className="group relative rounded-2xl border border-slate-800 bg-slate-900/35 backdrop-blur-md p-4 transition-all duration-300 hover:-translate-y-1 hover:border-cyan-500/30 hover:shadow-[0_10px_30px_-10px_rgba(34,211,238,0.15)] flex flex-col justify-between h-full">
      <Link to={`/products/${product.slug}`} className="block overflow-hidden rounded-xl bg-slate-950/40">
        <img
          src={imageUrl}
          alt={product.name}
          className="h-48 w-full object-cover transition-transform duration-500 group-hover:scale-105"
          loading="lazy"
        />
      </Link>

      <div className="mt-4 flex-grow flex flex-col justify-between">
        <div>
          <div className="flex items-center justify-between">
            <span className="text-xs font-semibold uppercase tracking-wider text-cyan-400">
              {category}
            </span>
            <span className="text-xs text-slate-400 font-medium">
              {brand}
            </span>
          </div>

          <Link to={`/products/${product.slug}`} className="mt-2 block">
            <h2 className="text-base font-bold text-white group-hover:text-cyan-400 transition-colors line-clamp-1">
              {product.name}
            </h2>
          </Link>

          {/* Rating Display */}
          <div className="mt-2 flex items-center gap-1.5">
            <div className="flex items-center text-amber-400">
              <svg className="h-4 w-4 fill-current" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            </div>
            <span className="text-xs font-bold text-slate-200">{rating.toFixed(1)}</span>
            <span className="text-xs text-slate-500">({product.reviewCount || 0})</span>
          </div>
        </div>

        <div className="mt-4 pt-3 border-t border-slate-800/80 flex items-center justify-between">
          <div className="flex items-baseline gap-2">
            <span className="text-lg font-extrabold text-white">${price.toFixed(2)}</span>
            {product.comparePrice && product.comparePrice > price && (
              <span className="text-xs text-slate-500 line-through">${product.comparePrice.toFixed(2)}</span>
            )}
          </div>

          <Link
            to={`/products/${product.slug}`}
            className="rounded-lg bg-slate-800 p-2 text-slate-300 hover:bg-cyan-500 hover:text-slate-950 transition-colors"
            aria-label="View Details"
          >
            <svg className="h-4 w-4 stroke-current fill-none stroke-2" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5L21 12m0 0l-7.5 7.5M21 12H3" />
            </svg>
          </Link>
        </div>
      </div>
    </article>
  );
}
