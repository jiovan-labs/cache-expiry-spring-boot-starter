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

package com.jiovan.spring.starter.cacheexpiry.config;


import java.util.Optional;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import com.jiovan.plexus.spring.toolkit.enumeration.CacheManagerType;
import com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor;
import com.jiovan.spring.starter.cacheexpiry.property.CacheExpiryProperties;
import com.jiovan.spring.starter.cacheexpiry.resolver.CacheNameResolver;
import com.jiovan.spring.starter.cacheexpiry.resolver.impl.TargetClassNameResolver;


/**
 * Configuration for cache expiry
 *
 * @author David Hsing
 * @see org.springframework.cache.annotation.AbstractCachingConfiguration
 * @see org.springframework.cache.annotation.ProxyCachingConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = CacheExpiryAutoConfiguration.PROPERTIES_PREFIX, name = "enabled", matchIfMissing = true)
@ConditionalOnClass(value = CacheManager.class)
@ConditionalOnBean(value = CacheAspectSupport.class)
@AutoConfigureAfter(value = CacheAutoConfiguration.class)
@Import(value = {CacheExpiryAutoConfiguration.Entry.class, CacheExpiryAutoConfiguration.Caffeine.class, CacheExpiryAutoConfiguration.Jcache.class, CacheExpiryAutoConfiguration.Redis.class})
public class CacheExpiryAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "jiovan.cache-expiry";    // $NON-NLS-1$
    public static final String CACHE_MANAGER = "cacheExpiryCacheManager";    // $NON-NLS-1$
    public static final String CACHE_RESOLVER = "cacheExpiryCacheResolver";    // $NON-NLS-1$


    @Order(value = 0)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Entry {
        /**
         * Returns a new {@link com.jiovan.spring.starter.cacheexpiry.property.CacheExpiryProperties} holding the cache expiry settings
         *
         * @return a new {@link com.jiovan.spring.starter.cacheexpiry.property.CacheExpiryProperties} bean registered when none is present
         */
        @Bean
        @ConditionalOnMissingBean
        public CacheExpiryProperties cacheExpiryProperties() {
            return new CacheExpiryProperties();
        }

        /**
         * Returns the {@link com.jiovan.spring.starter.cacheexpiry.resolver.CacheNameResolver} that derives cache names from the target class and method
         *
         * @param properties the cache expiry properties providing class name, method name, SpEL and prefix/suffix settings
         *
         * @return a {@link com.jiovan.spring.starter.cacheexpiry.resolver.impl.TargetClassNameResolver} configured from the given properties
         */
        @Bean
        @ConditionalOnBooleanProperty(prefix = PROPERTIES_PREFIX + ".cache-name-resolver", name = "enabled", matchIfMissing = true)
        @ConditionalOnMissingBean
        public CacheNameResolver targetClassNameResolver(@Nonnull CacheExpiryProperties properties) {
            CacheExpiryProperties.CacheNameResolver props = properties.getCacheNameResolver();
            TargetClassNameResolver result = new TargetClassNameResolver();
            result.setShortClassName(BooleanUtils.isTrue(props.getShortClassName()));
            result.setMethodName(BooleanUtils.isTrue(props.getMethodName()));
            result.setIndentMethodName(BooleanUtils.isTrue(props.getIndentMethodName()));
            result.setResolveSpelName(BooleanUtils.isTrue(props.getResolveSpelName()));
            result.setNamePrefix(props.getNamePrefix());
            result.setNameSuffix(props.getNameSuffix());
            return result;
        }
    }


    @Order(value = 1)
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "caffeine", matchIfMissing = true)
    @ConditionalOnClass(name = "com.github.benmanes.caffeine.cache.Caffeine")
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Caffeine {
        /**
         * Returns the {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} bound to Caffeine cache managers
         *
         * @param properties the cache expiry properties providing the processor order
         *
         * @return a {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} configured for {@link com.jiovan.plexus.spring.toolkit.enumeration.CacheManagerType#CAFFEINE}
         */
        @Bean
        @ConditionalOnMissingBean
        public CacheExpiryResolverProcessor cacheExpiryResolverProcessor(@Nonnull CacheExpiryProperties properties) {
            CacheExpiryResolverProcessor result = new CacheExpiryResolverProcessor(properties, CacheManagerType.CAFFEINE);
            Optional.ofNullable(properties.getCacheResolver().getProcessorOrder()).ifPresent(result::setOrder);
            return result;
        }
    }


    @Order(value = 2)
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "jcache", matchIfMissing = true)
    @ConditionalOnClass(name = "javax.cache.CacheManager")
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Jcache {
        /**
         * Returns the {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} bound to JCache cache managers
         *
         * @param properties the cache expiry properties providing the processor order
         *
         * @return a {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} configured for {@link com.jiovan.plexus.spring.toolkit.enumeration.CacheManagerType#JCACHE}
         */
        @Bean
        @ConditionalOnMissingBean
        public CacheExpiryResolverProcessor cacheExpiryResolverProcessor(@Nonnull CacheExpiryProperties properties) {
            CacheExpiryResolverProcessor result = new CacheExpiryResolverProcessor(properties, CacheManagerType.JCACHE);
            Optional.ofNullable(properties.getCacheResolver().getProcessorOrder()).ifPresent(result::setOrder);
            return result;
        }
    }


    @Order(value = 3)
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis", matchIfMissing = true)
    @ConditionalOnClass(name = "org.springframework.data.redis.core.RedisOperations")
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    static class Redis {
        /**
         * Returns the {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} bound to Redis cache managers
         *
         * @param properties the cache expiry properties providing the processor order
         *
         * @return a {@link com.jiovan.spring.starter.cacheexpiry.processor.CacheExpiryResolverProcessor} configured for {@link com.jiovan.plexus.spring.toolkit.enumeration.CacheManagerType#REDIS}
         */
        @Bean
        @ConditionalOnMissingBean
        public CacheExpiryResolverProcessor cacheExpiryResolverProcessor(@Nonnull CacheExpiryProperties properties) {
            CacheExpiryResolverProcessor result = new CacheExpiryResolverProcessor(properties, CacheManagerType.REDIS);
            Optional.ofNullable(properties.getCacheResolver().getProcessorOrder()).ifPresent(result::setOrder);
            return result;
        }
    }
}
