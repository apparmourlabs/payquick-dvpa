# PayQuick — Damn Vulnerable Payment App

A deliberately vulnerable Android UPI payment application built for **mobile security training, CTF challenges, and demonstrating automated SAST tools**.

> **WARNING:** This app contains intentional security vulnerabilities. Do NOT use any of this code in production. It exists solely for educational purposes.

## About

PayQuick simulates a realistic UPI-based payment application with common security flaws found in real-world fintech apps. The vulnerabilities are designed to be **non-obvious** — they mirror actual mistakes made by development teams, not textbook examples.

This project was created by [AppArmour Labs](https://apparmourlabs.com) to demonstrate what our AI-powered mobile security scanner detects.

## Download

- **APK:** [PayQuick-v2.3.1.apk](https://github.com/apparmourlabs/payquick-dvpa/releases/latest)
- **Scan Report:** [View Full Security Report](https://apparmourlabs.com/sample-report)

## Vulnerability Categories

The app contains vulnerabilities across all 6 OWASP MASVS categories:

| Category | Checks | Difficulty |
|----------|--------|------------|
| Data Storage | 5 | Easy - Medium |
| Cryptography | 3 | Medium |
| Network Security | 3 | Easy - Medium |
| Platform Security | 4 | Medium - Hard |
| Code Quality | 4 | Medium |
| App Resilience | 3 | Easy |

**Total: 22 vulnerabilities mapped to OWASP MASVS v2**

## Challenge

Can you find all 22 vulnerabilities? Try scanning with [AppArmour Labs](https://apparmourlabs.com) or do it manually.

### Hints

<details>
<summary>Data Storage (5 vulnerabilities)</summary>

- Look at how authentication tokens are stored
- Check what gets logged during normal operations
- Examine SharedPreferences configuration
- Review backup settings
- Watch what happens during transfers
</details>

<details>
<summary>Cryptography (3 vulnerabilities)</summary>

- Review the encryption implementation carefully
- Check how PINs are protected
- Look at random number generation
</details>

<details>
<summary>Network Security (3 vulnerabilities)</summary>

- Examine the API client initialization
- Review the network security configuration
- Check for certificate validation
</details>

<details>
<summary>Platform Security (4 vulnerabilities)</summary>

- Check exported components and their protections
- Review WebView configuration
- Examine deep link handling
- Look at content providers
</details>

<details>
<summary>Code Quality (4 vulnerabilities)</summary>

- Check build configuration
- Look for code obfuscation
- Review database queries
- Examine file path handling
</details>

<details>
<summary>App Resilience (3 vulnerabilities)</summary>

- Check the config.json for anti-tampering settings
- Look for root/emulator detection
- Review reverse engineering protections
</details>

## Regulatory Compliance

When scanned, PayQuick fails compliance checks against:
- **RBI** — Reserve Bank of India mobile banking guidelines
- **NPCI** — UPI security requirements
- **PCI-DSS v4.0** — Payment card data protection
- **DPDP Act 2023** — Digital personal data protection

## Building from Source

```bash
# Clone
git clone https://github.com/apparmourlabs/payquick-dvpa.git
cd payquick-dvpa

# Build (requires Android SDK)
./gradlew assembleDebug

# APK will be at app/build/outputs/apk/debug/app-debug.apk
```

## Scan It

Upload the APK to [AppArmour Labs](https://apparmourlabs.com/scan) for a free security assessment. The free tier covers Data Storage checks — upgrade to Pro for the full 22-check analysis.

## License

MIT — Use freely for education, training, and security research.

## Credits

Built by [AppArmour Labs](https://apparmourlabs.com) — AI-Powered Mobile App Security Analysis.
