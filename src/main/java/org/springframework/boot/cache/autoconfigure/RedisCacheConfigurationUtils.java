/*
 * Copyright (c) 2020 Jiovan Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.cache.autoconfigure;


import java.lang.reflect.InvocationTargetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.redis.cache.RedisCacheConfiguration;


/**
 * Utilities for redis cache
 *
 * @author David Hsing
 * @see org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
 * @see org.springframework.data.redis.cache.RedisCacheConfiguration
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RedisCacheConfigurationUtils {
    /**
     * Returns a {@link org.springframework.data.redis.cache.RedisCacheConfiguration} created from the given cache properties
     *
     * @param properties the Spring Boot cache properties used to build the configuration
     *
     * @return a {@link org.springframework.data.redis.cache.RedisCacheConfiguration} built from the given cache properties
     *
     * @throws java.lang.reflect.InvocationTargetException retained for source compatibility, never thrown
     * @throws java.lang.NoSuchMethodException retained for source compatibility, never thrown
     * @throws java.lang.IllegalAccessException retained for source compatibility, never thrown
     */
    @Nonnull
    public static RedisCacheConfiguration createConfiguration(@Nonnull CacheProperties properties) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return createConfiguration(properties, null);
    }

    /**
     * Returns a {@link org.springframework.data.redis.cache.RedisCacheConfiguration} created from the given cache properties and class loader
     *
     * @param properties the Spring Boot cache properties used to build the configuration
     * @param loader the class loader used to resolve cache-related classes, may be {@code null}
     *
     * @return a {@link org.springframework.data.redis.cache.RedisCacheConfiguration} built from the given cache properties
     *
     * @throws java.lang.reflect.InvocationTargetException retained for source compatibility, never thrown
     * @throws java.lang.NoSuchMethodException retained for source compatibility, never thrown
     * @throws java.lang.IllegalAccessException retained for source compatibility, never thrown
     */
    @Nonnull
    public static RedisCacheConfiguration createConfiguration(@Nonnull CacheProperties properties, @Nullable ClassLoader loader) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ClassLoader classLoader = (loader != null) ? loader : RedisCacheConfigurationUtils.class.getClassLoader();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig(classLoader);
        CacheProperties.Redis redis = properties.getRedis();
        if (redis.getTimeToLive() != null) {
            configuration = configuration.entryTtl(redis.getTimeToLive());
        }
        if (redis.getKeyPrefix() != null) {
            configuration = configuration.prefixCacheNameWith(redis.getKeyPrefix());
        }
        if (!redis.isCacheNullValues()) {
            configuration = configuration.disableCachingNullValues();
        }
        if (!redis.isUseKeyPrefix()) {
            configuration = configuration.disableKeyPrefix();
        }
        return configuration;
    }
}
