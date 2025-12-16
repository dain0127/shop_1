package com.changin.shop.repository;

import com.changin.shop.constant.ItemSellStatus;
import com.changin.shop.dto.ItemAdminDto;
import com.changin.shop.dto.ItemSearchDto;
import com.changin.shop.dto.MainItemDto;
import com.changin.shop.dto.QMainItemDto;
import com.changin.shop.entity.Item;
import com.changin.shop.entity.QItem;
import com.changin.shop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.changin.shop.entity.QItem.item;
import static com.changin.shop.entity.QItemImg.itemImg;


@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    //현재 ~ n일 전 날짜 필터
    private BooleanExpression searchRegDateAfter(String searchDateType){
        LocalDateTime startLocalDateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType)){
            return null;
        }else if(StringUtils.equals("1d", searchDateType)){
            startLocalDateTime = startLocalDateTime.minusDays(1);
        }else if(StringUtils.equals("1w", searchDateType)){
            startLocalDateTime = startLocalDateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m", searchDateType)){
            startLocalDateTime = startLocalDateTime.minusMonths(1);
        }else if(StringUtils.equals("6m", searchDateType)){
            startLocalDateTime = startLocalDateTime.minusMinutes(6);
        }else{
            return null;
        }

        return item.regTime.after(startLocalDateTime);
    }

    //판매 상태 필터
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
    }

    //검색어 필터
    private BooleanExpression searchByLike(String searchBy, String searchQuery) throws NoSuchFieldException {

        if(StringUtils.equals("itemNm", searchBy)){
            if(searchQuery == null || searchQuery.equals("null"))
                return null;
            return item.itemNm.like("%"+searchQuery +"%");
        }else if(StringUtils.equals("createdBy", searchBy)) {
            return item.createdBy.like("%"+ searchQuery+"%");
        }
        //최초 검색시 조건 없음
        else if (searchBy == null) {
            return null;
        } else {
            throw new NoSuchFieldException("검색 타입이 존재하는 데이터가 아닙니다.");
        }
    }

    @Override
    public Page<ItemAdminDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        try {
//            BooleanExpression bx = searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery());
//            String str;
//            if(bx!=null)
//                str = bx.toString();

            //list 반환
            List<ItemAdminDto> content = queryFactory
                    .select(Projections.constructor(
                            ItemAdminDto.class,
                            item.id,
                            item.itemNm,
                            item.itemSellStatus,
                            item.createdBy,
                            item.regTime,
                            itemImg.imgUrl     // ItemImg 의 대표 이미지 URL 필드
                    ))
                    .from(item)
                    .leftJoin(itemImg)
                    .on(itemImg.item.eq(item).and(itemImg.repImgYn.eq("Y")))
                    .where(searchRegDateAfter(itemSearchDto.getSearchDateType()),
                            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                    .orderBy(item.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            //전체 개수 조회
            Long total = queryFactory.select(Wildcard.count)
                    .from(item)
                    .where(searchRegDateAfter(itemSearchDto.getSearchDateType()),
                            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                    .fetchOne(); //단건 반환 (전체 그룹 집계함수)

            return new PageImpl<>(content, pageable, total);
        }catch (NoSuchFieldException e){
            log.info("=========================" + e.getMessage());
            List<ItemAdminDto> errorList = new ArrayList<>();
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<MainItemDto> content = null;
        try {
            content = queryFactory
                    .select(
                            Projections.constructor(
                                    MainItemDto.class,
                                    QItem.item.id,
                                    QItem.item.itemNm,
                                    QItem.item.itemDetail,
                                    QItemImg.itemImg.imgUrl,
                                    QItem.item.price
                            )
                    )
                    .from(item)
                    .leftJoin(itemImg)
                    .on(itemImg.item.eq(item).and(itemImg.repImgYn.eq("Y")))
                    .where(searchRegDateAfter(itemSearchDto.getSearchDateType()),
                            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                    .orderBy(QItem.item.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            //전체 개수 조회
            Long total = queryFactory.select(Wildcard.count)
                    .from(item)
                    .where(searchRegDateAfter(itemSearchDto.getSearchDateType()),
                            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                            searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                    .fetchOne(); //단건 반환 (전체 그룹 집계함수)

            return new PageImpl<MainItemDto>(content, pageable, total);
        } catch (NoSuchFieldException e) {
            log.info("=========================" + e.getMessage());
            List<ItemAdminDto> errorList = new ArrayList<>();
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }


    }
}
