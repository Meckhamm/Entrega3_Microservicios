@Test
void crearRechazaProductoRemotoInexistente() {
    InventarioDTO dto = dtoValido();

    when(productoClient.obtenerProducto(dto.getProductoId()))
            .thenThrow(feignNotFound());

    assertThrows(BusinessRuleException.class,
            () -> service.crear(dto));

    verify(repository, never()).save(any(Inventario.class));
}

@Test
void eliminarEliminaInventarioExistente() {
    Inventario inventario = inventario(1L, 10L);

    when(repository.findById(1L))
            .thenReturn(Optional.of(inventario));

    service.eliminar(1L);

    assertEquals(1L, inventario.getId());

    verify(repository).delete(inventario);
}

private InventarioDTO dtoValido() {
    ...
}

private Inventario inventario(Long id, Long productoId) {
    ...
}

private FeignException feignNotFound() {
    ...
	}
