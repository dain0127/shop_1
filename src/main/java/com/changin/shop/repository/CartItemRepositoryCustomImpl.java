package com.changin.shop.repository;

import com.changin.shop.common.entity.QBaseTimeEntity;
import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.CartDetailDto;
import com.changin.shop.dto.ItemAdminDto;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.entity.QCartItem;
import com.changin.shop.entity.QItem;
import com.changin.shop.entity.QItemImg;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.changin.shop.common.entity.QBaseTimeEntity.baseTimeEntity;
import static com.changin.shop.entity.QCart.cart;
import static com.changin.shop.entity.QCartItem.cartItem;
import static com.changin.shop.entity.QItem.item;
import static com.changin.shop.entity.QItemImg.itemImg;
import static com.changin.shop.entity.QMember.member;


@Slf4j
public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public CartItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartDetailDto> findCartDetailDtoList(Long cartId,
                                                     String email){
         List<CartDetailDto> content;
         content = queryFactory
                 .select(Projections.constructor(
                         CartDetailDto.class,
                         cartItem.id,
                         item.itemNm ,
                         item.price,
                         cartItem.count,
                         itemImg.imgUrl
                 ))
                 .from(cartItem)
                 .join(cartItem.item, item)
                 .join(itemImg).on(itemImg.item.eq(item))
                 .join(cartItem.cart, cart)
                 .join(cart.member, member)
                 .where(member.email.eq(email)
                        , itemImg.repImgYn.eq("Y"))
                 .orderBy(cartItem.updateTime.desc())
                 .fetch();

        return content;
    }
}
