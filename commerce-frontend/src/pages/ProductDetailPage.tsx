import { useParams, Link } from 'react-router-dom';
import { useProduct } from '../services/productService';

export default function ProductDetailPage() {
  const { slug } = useParams<{ slug: string }>();
  
  // Fetch product data by slug
  const { data: product, isLoading, isError } = useProduct(slug || '');

  if (isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-[#030712] text-white">
        <div className="flex flex-col items-center gap-3">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-cyan-400 border-t-transparent"></div>
          <p className="text-sm text-slate-400 animate-pulse">Loading product details...</p>
        </div>
      </div>
    );
  }

  if (isError || !product) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-[#030712] text-white p-6">
        <div className="rounded-2xl border border-red-500/20 bg-red-500/10 p-8 max-w-md text-center">
          <h2 className="text-xl font-bold text-red-400 mb-2">Product Not Found</h2>
          <p className="text-sm text-slate-400 mb-6">The product you are looking for might have been removed or is temporarily unavailable.</p>
          <Link
            to="/products"
            className="rounded-xl bg-cyan-400 px-6 py-2.5 text-sm font-semibold text-slate-950 hover:bg-cyan-300 transition-colors"
          >
            Back to Products
          </Link>
        </div>
      </div>
    );
  }

  const price = product.price || 0;
  const rating = product.rating || 0;
  const brand = product.brand || 'Generic';
  const category = product.category || 'Uncategorized';
  const imageUrl = product.imageUrl || 'https://images.unsplash.com/photo-1531403009284-440f080d1e12?q=80&w=600&auto=format&fit=crop';

  return (
    <main className="min-h-screen bg-[#030712] text-white py-12 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
      {/* Glow Effects */}
      <div className="absolute top-20 left-10 w-80 h-80 bg-cyan-500/5 rounded-full blur-[110px] pointer-events-none"></div>
      <div className="absolute bottom-20 right-10 w-96 h-96 bg-indigo-500/5 rounded-full blur-[130px] pointer-events-none"></div>

      <div className="mx-auto max-w-7xl relative">
        {/* Back Link */}
        <Link
          to="/products"
          className="inline-flex items-center gap-2 text-sm font-semibold text-slate-400 hover:text-cyan-400 transition-colors mb-8 group"
        >
          <svg className="h-4 w-4 stroke-current fill-none stroke-2 transition-transform group-hover:-translate-x-1" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
          Back to Catalog
        </Link>

        {/* Main Product Layout */}
        <div className="grid gap-12 lg:grid-cols-2 bg-slate-900/10 border border-slate-800/60 rounded-3xl p-6 md:p-10 backdrop-blur-sm">
          {/* Left: Image Container */}
          <div className="flex flex-col gap-4">
            <div className="overflow-hidden rounded-2xl bg-slate-950/40 border border-slate-800/50 flex items-center justify-center p-4">
              <img
                src={imageUrl}
                alt={product.name}
                className="w-full max-h-[450px] object-contain rounded-xl transition-all duration-300"
              />
            </div>
          </div>

          {/* Right: Info and Pricing Container */}
          <div className="flex flex-col justify-between space-y-6">
            <div>
              <div className="flex items-center justify-between">
                <span className="rounded-full bg-cyan-500/10 px-3.5 py-1 text-xs font-bold uppercase tracking-wider text-cyan-400 border border-cyan-500/20">
                  {category}
                </span>
                <span className="text-sm font-semibold text-slate-400">{brand}</span>
              </div>

              <h1 className="mt-4 text-3xl md:text-4xl font-extrabold tracking-tight text-white leading-tight">
                {product.name}
              </h1>

              {/* Rating and review section */}
              <div className="mt-4 flex items-center gap-3">
                <div className="flex items-center text-amber-400 gap-0.5">
                  <svg className="h-5 w-5 fill-current" viewBox="0 0 20 20">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                  </svg>
                  <span className="text-sm font-bold text-slate-200">{rating.toFixed(1)}</span>
                </div>
                <span className="text-slate-600">|</span>
                <span className="text-sm text-slate-400 font-medium">
                  {product.reviewCount || 0} customer reviews
                </span>
              </div>
            </div>

            {/* Pricing Section */}
            <div className="py-4 border-y border-slate-800/80">
              <div className="flex items-baseline gap-4">
                <span className="text-4xl font-extrabold text-white">${price.toFixed(2)}</span>
                {product.comparePrice && product.comparePrice > price && (
                  <span className="text-lg text-slate-500 line-through">${product.comparePrice.toFixed(2)}</span>
                )}
              </div>
              <p className="mt-2 text-xs text-emerald-400 font-medium">Free shipping on orders over $50</p>
            </div>

            {/* Description */}
            <div>
              <h3 className="text-sm font-bold uppercase tracking-wider text-slate-400 mb-2">Description</h3>
              <p className="text-slate-300 text-sm leading-relaxed whitespace-pre-line">
                {product.description || product.shortDescription || 'No description available for this product.'}
              </p>
            </div>

            {/* Specifications Summary Table */}
            <div>
              <h3 className="text-sm font-bold uppercase tracking-wider text-slate-400 mb-2">Specifications</h3>
              <div className="rounded-xl border border-slate-800 overflow-hidden text-sm">
                <div className="flex bg-slate-950/40 border-b border-slate-800">
                  <div className="w-1/3 px-4 py-3 font-semibold text-slate-400 border-r border-slate-800">SKU</div>
                  <div className="w-2/3 px-4 py-3 text-white font-mono">{product.sku}</div>
                </div>
                <div className="flex bg-slate-900/10 border-b border-slate-800">
                  <div className="w-1/3 px-4 py-3 font-semibold text-slate-400 border-r border-slate-800">Brand</div>
                  <div className="w-2/3 px-4 py-3 text-white">{brand}</div>
                </div>
                <div className="flex bg-slate-950/40">
                  <div className="w-1/3 px-4 py-3 font-semibold text-slate-400 border-r border-slate-800">Availability</div>
                  <div className="w-2/3 px-4 py-3">
                    {product.stockQuantity > 0 ? (
                      <span className="text-emerald-400 font-bold">In Stock ({product.stockQuantity} available)</span>
                    ) : (
                      <span className="text-red-400 font-bold">Out of Stock</span>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Related Products Placeholder */}
        <section className="mt-16 pt-12 border-t border-slate-800/80">
          <h2 className="text-2xl font-bold text-white mb-6">Related Products</h2>
          <div className="grid gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
            {/* 4 related placeholders cards */}
            {Array.from({ length: 4 }).map((_, i) => (
              <div
                key={i}
                className="rounded-2xl border border-slate-800 bg-slate-900/10 p-5 flex flex-col items-center justify-center h-48 text-center text-slate-500 backdrop-blur-sm"
              >
                <svg className="h-8 w-8 stroke-current fill-none stroke-1.5 mb-2" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z" />
                </svg>
                <p className="text-xs font-semibold uppercase tracking-wider">Related Product {i + 1}</p>
                <p className="text-[11px] text-slate-600 mt-1">Recommendations coming soon</p>
              </div>
            ))}
          </div>
        </section>
      </div>
    </main>
  );
}
