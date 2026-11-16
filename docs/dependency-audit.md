# Dependency audit — 2026-11-16

Updated every version in the Gradle catalog to the latest stable version published in Google Maven or Maven Central; JUnit 4.13.2 and Arch Core Testing 2.2.0 were already current. Compose libraries remain aligned by BOM 2026.09.00 (including the upstream-frozen Material Icons 1.7.8). Updated release workflow actions to their latest stable GitHub releases.

The build now uses AGP 9.5.1, Kotlin 2.4.21, KSP 2.3.14, Gradle 9.9.1 and compile SDK 37. It uses AGP's built-in Kotlin support and the current Hilt ViewModel Compose package. Minimum SDK 26 and target SDK 35 are unchanged.

## Vulnerability results

Queried the [OSV batch API](https://google.github.io/osv.dev/api/#post-v1querybatch) for exact Maven package/version pairs. The final scan covered **332 distinct resolved coordinates**, with **zero known advisories returned**. See [machine-readable results](dependency-audit.json) for the complete inventory, timestamp and responses.

Coverage includes debug/release compile and runtime dependencies, unit/instrumentation test dependencies, KSP processor classpaths, and root/module buildscript classpaths. Dependency graph resolution was checked for unresolved dependencies. This checks published dependency advisories; it does not audit application logic, Gradle's embedded libraries, SDK binaries or GitHub Action internals.

The original Kotlin Gradle plugin 2.1.0 matched [GHSA-r937-wjx7-w2jp / CVE-2026-53914](https://osv.dev/vulnerability/GHSA-r937-wjx7-w2jp), unsafe deserialization of build-cache metadata. Kotlin 2.4.21 resolves it.

Even the latest build plugins and processors requested vulnerable transitive versions. Added buildscript constraints and a KSP Guava constraint, then verified their resolved versions:

| Build dependency | Resolved patched version | Advisories addressed |
| --- | --- | --- |
| Guava | 33.8.0-jre | [GHSA-xxph-c9ww-hj94](https://osv.dev/vulnerability/GHSA-xxph-c9ww-hj94) |
| Commons Lang | 3.22.0 | [GHSA-j288-q9x7-2f5v](https://osv.dev/vulnerability/GHSA-j288-q9x7-2f5v) |
| jose4j | 0.9.7 | [GHSA-3677-xxcr-wjqv](https://osv.dev/vulnerability/GHSA-3677-xxcr-wjqv) |
| Bouncy Castle (provider, PKIX and utilities) | 1.87 | [GHSA-wg6q-6289-32hp](https://osv.dev/vulnerability/GHSA-wg6q-6289-32hp), [GHSA-9pwp-9qqc-pr26](https://osv.dev/vulnerability/GHSA-9pwp-9qqc-pr26), [GHSA-qp49-qgx5-5m26](https://osv.dev/vulnerability/GHSA-qp49-qgx5-5m26) |
| JDOM | 2.0.6.1 | [GHSA-2363-cqg2-863c](https://osv.dev/vulnerability/GHSA-2363-cqg2-863c) |

These transitive findings were confined to build tools/code generators, not the app runtime. Remove the constraints once upstream dependencies request patched versions, and repeat the audit after future updates.

## Validation

Final validation command:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug assembleRelease assembleDebugAndroidTest --max-workers=4
```

Passed: all 20 unit tests (zero failures/errors), Android lint, debug APK, minified release APK, and instrumentation-test APK builds.

Instrumentation tests require a device/emulator; their APK is compiled here but device execution is not performed. Validation uses a temporary JDK 17 and Android SDK, without changing the developer's local SDK configuration. The release build uses the project's existing debug-signing fallback when signing credentials are absent.

Version/toolchain references: [AGP 9.5 compatibility](https://developer.android.com/build/releases/agp-9-5-0-release-notes), [built-in Kotlin migration](https://developer.android.com/build/migrate-to-built-in-kotlin), [Compose BOM](https://developer.android.com/develop/ui/compose/bom).
