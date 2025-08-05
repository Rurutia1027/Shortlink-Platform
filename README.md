# 🚀 ShortLink SaaS Platform | [![SaaS Short Link Platform Build](https://github.com/Rurutia1027/shortlink-platform/actions/workflows/ci.yml/badge.svg?branch=main&event=status)](https://github.com/Rurutia1027/shortlink-platform/actions/workflows/ci.yml)

A scalable, high-performance SaaS short link system designed for enterprise-grade usage. Built with modern Java stack (
JDK 17, Spring Boot 3, Spring Cloud), it provides secure, efficient, and observable short link services including link
generation, redirection, expiration, statistics, and tenant isolation.

-----

## 🌐 What Is a Short Link System?

A short link (short URL) maps a long original URL to a shorter one, making it easier to share and track. The system
generates a unique identifier that redirects to the original URL when accessed.

### Process Overview:

1. **Generate short code** via hash or sequential ID algorithms
2. **Store mapping** between short code and original URL in DB/cache
3. **Build redirect link** like `https://s.shortlink.cn/A9x3YpZ`
4. **Redirect user** via HTTP 302
5. **Collect metrics** such as PV, UV, IP, referrer, device, etc.

-----

## ✨ Key Features

- 🔗 Short URL generation / redirection / QR code support
- ⏱ Expiration & rate-limiting policies
- 🔁 Logical recycle bin for expired/deleted links
- 📊 Real-time analytics: PV/UV/IP/Geo/Device
- 🔒 Multi-tenant architecture with permission isolation
- ⚡ Multi-level cache: local + Redis with Bloom filter
- 💥 Cache penetration prevention and async prewarm
- 📱 App deep linking / QR code generation
- 🖥️ Admin dashboard with SaaS-style tenant management

------

## 🧱 System Architecture

| Layer             | Technologies                                       |
|-------------------|----------------------------------------------------|
| Frontend          | Vue 3 + Vite + Element Plus                        |
| API Gateway       | Spring Cloud Gateway + JWT Auth                    |
| Backend Services  | Spring Boot 3 + Spring Cloud + Microservices       |
| Persistence       | MyBatis Plus + MySQL (sharding) + HBase (optional) |
| Cache Layer       | Redis + Local Cache + Bloom Filter                 |
| Messaging         | RocketMQ (for async stats, expiration, logs)       |
| Registry & Config | Nacos                                              |
| Observability     | Sleuth + Zipkin                                    |
| DevOps            | Docker + Jenkins + Nginx + Kubernetes (optional)   |

---

## 📈 Performance Testing (Alibaba Cloud PTS)

| Metric  | Scenario               | Peak Value   |
|---------|------------------------|--------------|
| TPS     | Short link creation    | 12,000 / sec |
| QPS     | Short link redirection | 56,000 / sec |
| TP999   | P99 latency            | ≤ 7 ms       |
| Redis   | Cache memory usage     | 1.2 TB       |
| Storage | Total data (HBase)     | 4 TB         |

✅ Proven performance under real-world high-concurrency scenarios.

---

## 📊 Operational Statistics (Production Deployment)

- 🌍 **Daily Short Links Generated**: 100 million+
- 🚀 **Daily Redirection Requests**: 80 million+
- 🛠️ **TP999 Redirection Latency**: < 7 ms
- 📡 **Live Metrics**: PV, UV, IP, OS, browser, region, referer

---

## 🔐 Security & Compliance

- ✅ IP blacklists / whitelists
- ✅ Anti-spam & rate-limiting
- ✅ Secure signature validation
- ✅ Multi-tenant data isolation
- ✅ Custom redirect domain support

---