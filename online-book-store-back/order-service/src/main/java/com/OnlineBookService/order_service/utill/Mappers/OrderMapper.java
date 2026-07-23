package com.OnlineBookService.order_service.utill.Mappers;

import com.OnlineBookService.order_service.dto.BookQuantityRequestDto;
import com.OnlineBookService.order_service.dto.OrderResponseDto;
import com.OnlineBookService.order_service.entity.Order;
import com.OnlineBookService.order_service.entity.OrderItem;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    List<OrderResponseDto> orderPageToOrderResponseDtoList(Page<Order> orders);
    List<BookQuantityRequestDto> orderItemsListToBookQuantittyRequestDtoList(List<OrderItem> orders);
}
