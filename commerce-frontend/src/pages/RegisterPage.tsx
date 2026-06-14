import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

export default function RegisterPage() {
  const { register, loading } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState<{
    firstName?: string;
    lastName?: string;
    email?: string;
    phone?: string;
    password?: string;
    confirmPassword?: string;
    form?: string;
  }>({});

  const [successMsg, setSuccessMsg] = useState('');

  const validate = () => {
    const newErrors: typeof errors = {};

    if (!form.firstName.trim()) {
      newErrors.firstName = 'First name is required';
    }
    if (!form.lastName.trim()) {
      newErrors.lastName = 'Last name is required';
    }
    if (!form.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(form.email)) {
      newErrors.email = 'Please enter a valid email address';
    }
    if (form.phone.trim() && !/^\+?[0-9]{7,15}$/.test(form.phone.trim())) {
      newErrors.phone = 'Please enter a valid phone number (7-15 digits)';
    }
    if (!form.password) {
      newErrors.password = 'Password is required';
    } else if (form.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters';
    } else if (form.password.length > 72) {
      newErrors.password = 'Password must not exceed 72 characters';
    }
    if (!form.confirmPassword) {
      newErrors.confirmPassword = 'Confirm password is required';
    } else if (form.password !== form.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      setErrors({});
      setSuccessMsg('');

      const payload = {
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim(),
        email: form.email.trim().toLowerCase(),
        phone: form.phone.trim() || null,
        password: form.password,
      };

      await register(payload);
      setSuccessMsg('User registered successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err: any) {
      setErrors({
        form: err.response?.data?.message || 'Registration failed. Please try again.',
      });
    }
  };

  const handleChange = (field: keyof typeof form, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  return (
    <main className="relative flex min-h-screen items-center justify-center p-6 bg-[#030712] overflow-hidden">
      {/* Background gradients for ambient glow */}
      <div className="absolute top-1/4 left-1/4 -translate-x-1/2 -translate-y-1/2 w-80 h-80 bg-cyan-500/10 rounded-full blur-[100px] pointer-events-none"></div>
      <div className="absolute bottom-1/4 right-1/4 translate-x-1/2 translate-y-1/2 w-96 h-96 bg-indigo-500/10 rounded-full blur-[120px] pointer-events-none"></div>

      <section className="relative w-full max-w-2xl rounded-3xl border border-slate-800 bg-slate-900/40 backdrop-blur-xl p-8 shadow-[0_20px_50px_rgba(0,0,0,0.5)] hover:border-cyan-500/20 transition-all duration-500">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold bg-gradient-to-r from-white via-slate-200 to-slate-400 bg-clip-text text-transparent">
            Create an Account
          </h1>
          <p className="mt-2 text-sm text-slate-400">Get started with our Smart Commerce Platform</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          {errors.form && (
            <div className="rounded-xl border border-red-500/20 bg-red-500/10 p-4 text-sm text-red-400 animate-shake">
              {errors.form}
            </div>
          )}

          {successMsg && (
            <div className="rounded-xl border border-emerald-500/20 bg-emerald-500/10 p-4 text-sm text-emerald-400">
              {successMsg}
            </div>
          )}

          <div className="grid gap-5 md:grid-cols-2">
            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                First Name
              </label>
              <input
                type="text"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.firstName ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="John"
                value={form.firstName}
                onChange={(e) => handleChange('firstName', e.target.value)}
              />
              {errors.firstName && <p className="mt-1 text-xs text-red-400">{errors.firstName}</p>}
            </div>

            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                Last Name
              </label>
              <input
                type="text"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.lastName ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="Doe"
                value={form.lastName}
                onChange={(e) => handleChange('lastName', e.target.value)}
              />
              {errors.lastName && <p className="mt-1 text-xs text-red-400">{errors.lastName}</p>}
            </div>
          </div>

          <div className="grid gap-5 md:grid-cols-2">
            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                Email Address
              </label>
              <input
                type="text"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.email ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="john@example.com"
                value={form.email}
                onChange={(e) => handleChange('email', e.target.value)}
              />
              {errors.email && <p className="mt-1 text-xs text-red-400">{errors.email}</p>}
            </div>

            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                Phone Number (Optional)
              </label>
              <input
                type="text"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.phone ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="+919876543210"
                value={form.phone}
                onChange={(e) => handleChange('phone', e.target.value)}
              />
              {errors.phone && <p className="mt-1 text-xs text-red-400">{errors.phone}</p>}
            </div>
          </div>

          <div className="grid gap-5 md:grid-cols-2">
            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                Password
              </label>
              <input
                type="password"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.password ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="••••••••"
                value={form.password}
                onChange={(e) => handleChange('password', e.target.value)}
              />
              {errors.password && <p className="mt-1 text-xs text-red-400">{errors.password}</p>}
            </div>

            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">
                Confirm Password
              </label>
              <input
                type="password"
                className={`w-full rounded-xl border bg-slate-950/60 px-4 py-3 text-white transition-all outline-none focus:ring-2 focus:ring-cyan-500/50 ${
                  errors.confirmPassword ? 'border-red-500/50 focus:ring-red-500/30' : 'border-slate-800 focus:border-cyan-500'
                }`}
                placeholder="••••••••"
                value={form.confirmPassword}
                onChange={(e) => handleChange('confirmPassword', e.target.value)}
              />
              {errors.confirmPassword && <p className="mt-1 text-xs text-red-400">{errors.confirmPassword}</p>}
            </div>
          </div>

          <button
            type="submit"
            className="relative w-full mt-2 rounded-xl bg-gradient-to-r from-cyan-400 to-indigo-500 py-3 font-semibold text-slate-950 hover:opacity-90 active:scale-[0.98] transition-all disabled:opacity-50 disabled:pointer-events-none overflow-hidden"
            disabled={loading}
          >
            {loading ? (
              <span className="flex items-center justify-center gap-2">
                <span className="h-4 w-4 animate-spin rounded-full border-2 border-slate-950 border-t-transparent"></span>
                Creating account...
              </span>
            ) : (
              'Register'
            )}
          </button>
        </form>

        <div className="mt-8 text-center text-sm text-slate-400">
          Already have an account?{' '}
          <Link to="/login" className="font-semibold text-cyan-400 hover:underline">
            Login here
          </Link>
        </div>
      </section>
    </main>
  );
}
