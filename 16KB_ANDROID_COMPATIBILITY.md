# 16KB Android (Android Go) Compatibility

This Flutter Twilio Conversations plugin has been optimized for 16KB Android (Android Go edition) devices, which have strict memory and APK size constraints.

## What is 16KB Android?

16KB Android, also known as Android Go, is designed for devices with:
- 1GB or less of RAM
- Limited storage capacity
- Budget hardware constraints
- Stricter APK size requirements

## Optimizations Implemented

### 1. Multidex Support
- **AndroidManifest.xml**: Configured to use `androidx.multidex.MultiDexApplication`
- **build.gradle**: Enabled multidex for both debug and release builds
- Prevents the 64K method limit issue common with large dependencies

### 2. ProGuard/R8 Code Shrinking
- **Release builds**: Enabled `minifyEnabled` and `shrinkResources`
- **proguard-rules.pro**: Comprehensive rules for Twilio SDK and Flutter plugin compatibility
- Aggressive optimization settings for maximum code reduction

### 3. Updated Dependencies
- **Twilio Conversations SDK**: Updated to v4.0.3 for better optimization
- **Kotlin Coroutines**: Updated to v1.6.4 for improved performance
- **Android Gradle Plugin**: Updated to v7.4.2 for better R8 support
- **Kotlin**: Updated to v1.8.22 for latest optimizations

### 4. Build Optimizations
- **R8 Full Mode**: Enabled for maximum code shrinking
- **Incremental builds**: Enabled for faster development cycles
- **Parallel processing**: Enabled for faster builds
- **Build caching**: Enabled to speed up repeated builds

### 5. APK Size Reduction
- **Packaging optimizations**: Excludes unnecessary META-INF files
- **Resource shrinking**: Automatically removes unused resources in release builds
- **Lint optimizations**: Configured to ignore non-critical warnings for 16KB Android

## Usage Instructions

### For App Developers

When using this plugin in your Flutter app targeting 16KB Android devices:

1. **Enable multidex in your app's `android/app/build.gradle`**:
```gradle
android {
    defaultConfig {
        multiDexEnabled true
    }
}
```

2. **Update your app's `AndroidManifest.xml`** (if not already done):
```xml
<application
    android:name="androidx.multidex.MultiDexApplication">
</application>
```

3. **Add ProGuard rules to your app** for Twilio compatibility by copying the rules from this plugin's `proguard-rules.pro`.

4. **Enable R8/ProGuard in your app's build.gradle**:
```gradle
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Build Configuration

The plugin is pre-configured with the following optimizations:

- **Compile SDK**: Updated to API 33 for latest optimizations
- **Min SDK**: Maintained at API 19 for broad compatibility  
- **Build Types**: Separate optimization for debug and release builds
- **Kotlin**: Configured for optimal bytecode generation

## Testing on 16KB Android Devices

### Recommended Test Devices
- Android Go devices (Nokia 1, Samsung Galaxy J2 Core, etc.)
- Emulator with 1GB RAM configuration
- Physical devices with limited memory

### Performance Monitoring
Monitor the following metrics:
- **APK size**: Should be significantly reduced in release builds
- **Method count**: Verify staying under DEX limits
- **Memory usage**: Monitor runtime memory consumption
- **App startup time**: Ensure acceptable performance

## Troubleshooting

### Common Issues

1. **Build failures after updating**:
   - Clean and rebuild: `flutter clean && flutter pub get`
   - Clear Gradle caches: `./gradlew clean`

2. **ProGuard/R8 issues**:
   - Check `proguard-rules.pro` for missing keep rules
   - Enable verbose ProGuard output for debugging

3. **Multidex issues**:
   - Ensure both plugin and app have multidex enabled
   - Check for conflicting multidex configurations

4. **Memory issues on 16KB devices**:
   - Monitor memory usage with Android Studio Profiler
   - Consider lazy loading of Twilio features

### Best Practices

1. **Test early and often** on actual 16KB Android devices
2. **Monitor APK size** with each build using `flutter build apk --analyze-size`
3. **Profile memory usage** during development
4. **Use release builds** for accurate performance testing
5. **Keep dependencies minimal** in your main app

## Technical Details

### Method Count Optimization
The plugin uses several strategies to reduce method count:
- ProGuard/R8 dead code elimination
- Kotlin bytecode optimization
- Dependency version alignment
- Aggressive shrinking in release builds

### Memory Optimization
- Lazy initialization of Twilio SDK components
- Efficient resource management
- Multidex to split DEX files efficiently

## Version Compatibility

This optimization is compatible with:
- **Flutter**: 3.0.0 and above
- **Android**: API 19 (Android 4.4) and above
- **Twilio Conversations SDK**: 4.0.3
- **Android Gradle Plugin**: 7.4.2

## Support

For issues related to 16KB Android compatibility:
1. Check this documentation first
2. Test on an actual 16KB Android device
3. Review ProGuard logs for any optimization issues
4. File issues with detailed device information and logs
