package br.com.diamante.order.repository;

import br.com.diamante.order.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
