package com.coffeestore.catalog;

/**
 * Simple DTO for Day 1.
 * Using cents (int) to avoid floating point issues for money.
 */
public record Product(Long id, String name, String description, int priceCents) {}
