package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.dto.response.StoreResponseForNativeQuery;
import com.example.outsourcing.domain.store.entity.Store;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryForSearch {

    @Query("SELECT COUNT(s) " +
            "FROM Store s " +
            "WHERE s.user.id = :userId")
    long countStoresByUserId(Long userId);

    @Query(value = "select * " +
            "  from ( " +
            "            select s.id as id " +
            "                 , s.address as address " +
            "                 , s.category_id as category_id " +
            "                 , c.category_name as category_name " +
            "                 , s.close_time as close_time " +
            "                 , s.created_at as created_at " +
            "                 , s.image_id as image_id " +
            "                 , s.min_order_price as min_order_price " +
            "                 , s.modified_at as modified_at " +
            "                 , s.open_time as open_time " +
            "                 , s.store_name as store_name " +
            "                 , s.store_notice as store_notice " +
            "                 , s.store_status as store_status " +
            "                 , s.user_id as user_id " +
            "                 , cast(st_distance_sphere(s.location, :location) as decimal(53, 20)) as distance " +
            "                 , cast(coalesce(avg(r.rate), 0) as decimal(53, 20))  as rate " +
            "              from store s " +
            "             inner join category c  " +
            "                     on c.id=s.category_id  " +
            "              left join orders o " +
            "                     on o.store_id=s.id  " +
            "              left join review r " +
            "                     on r.order_id=o.id  " +
            "             where ( " +
            "                        s.store_status != 'SHUTDOWN' " +
            "                   )           " +
            "               and o.order_status = :orderStatus " +
            "               and s.store_status = :storeStatus  " +
            "               and s.store_name like concat(:keyword, '%')   " +
            "               and st_contains(st_buffer(:location, 4000), s.location)  " +
            "             group by s.id  " +
            "              " +
            "             union all " +
            "              " +
            "             select s.id as id " +
            "                 , s.address as address " +
            "                 , s.category_id as category_id " +
            "                 , c.category_name as category_name " +
            "                 , s.close_time as close_time " +
            "                 , s.created_at as created_at " +
            "                 , s.image_id as image_id " +
            "                 , s.min_order_price as min_order_price " +
            "                 , s.modified_at as modified_at " +
            "                 , s.open_time as open_time " +
            "                 , s.store_name as store_name " +
            "                 , s.store_notice as store_notice " +
            "                 , s.store_status as store_status " +
            "                 , s.user_id as user_id " +
            "                 , cast(st_distance_sphere(s.location, :location) as decimal(53, 20)) as distance " +
            "                 , cast(coalesce(avg(r.rate), 0) as decimal(53, 20))  as rate " +
            "              from store s " +
            "             inner join category c  " +
            "                     on c.id=s.category_id  " +
            "              left join orders o " +
            "                     on o.store_id=s.id  " +
            "              left join review r " +
            "                     on r.order_id=o.id  " +
            "              left join menu m " +
            "                     on m.store_id=s.id  " +
            "             where ( " +
            "                     s.store_status != 'SHUTDOWN' " +
            "                   )           " +
            "               and o.order_status = :orderStatus " +
            "               and s.store_status = :storeStatus  " +
            "               and m.menu_name like concat(:keyword, '%')  " +
            "               and st_contains(st_buffer(:location, 4000), s.location)  " +
            "             group by s.id  " +
            "     ) total " +
            "group by total.id " +
            "order by total.distance " +
            "limit :offset, :limit ",
    nativeQuery = true)
    List<StoreResponseForNativeQuery> searchOrderByDistance(Point location, String keyword, String orderStatus, String storeStatus, long offset, int limit);

    @Query(value = "select * " +
            "  from ( " +
            "            select s.id as id " +
            "                 , s.address as address " +
            "                 , s.category_id as category_id " +
            "                 , c.category_name as category_name " +
            "                 , s.close_time as close_time " +
            "                 , s.created_at as created_at " +
            "                 , s.image_id as image_id " +
            "                 , s.min_order_price as min_order_price " +
            "                 , s.modified_at as modified_at " +
            "                 , s.open_time as open_time " +
            "                 , s.store_name as store_name " +
            "                 , s.store_notice as store_notice " +
            "                 , s.store_status as store_status " +
            "                 , s.user_id as user_id " +
            "                 , cast(st_distance_sphere(s.location, :location) as decimal(53, 20)) as distance " +
            "                 , cast(coalesce(avg(r.rate), 0) as decimal(53, 20))  as rate " +
            "              from store s " +
            "             inner join category c  " +
            "                     on c.id=s.category_id  " +
            "              left join orders o " +
            "                     on o.store_id=s.id  " +
            "                    and o.order_status = :orderStatus " +
            "              left join review r " +
            "                     on r.order_id=o.id  " +
            "             where ( " +
            "                        s.store_status != 'SHUTDOWN' " +
            "                   )           " +
            "               and s.store_status = :storeStatus  " +
            "               and s.store_name like concat(:keyword, '%')   " +
            "               and st_contains(st_buffer(:location, 4000), s.location)  " +
            "             group by s.id  " +
            "              " +
            "             union all " +
            "              " +
            "             select s.id as id " +
            "                 , s.address as address " +
            "                 , s.category_id as category_id " +
            "                 , c.category_name as category_name " +
            "                 , s.close_time as close_time " +
            "                 , s.created_at as created_at " +
            "                 , s.image_id as image_id " +
            "                 , s.min_order_price as min_order_price " +
            "                 , s.modified_at as modified_at " +
            "                 , s.open_time as open_time " +
            "                 , s.store_name as store_name " +
            "                 , s.store_notice as store_notice " +
            "                 , s.store_status as store_status " +
            "                 , s.user_id as user_id " +
            "                 , cast(st_distance_sphere(s.location, :location) as decimal(53, 20)) as distance " +
            "                 , cast(coalesce(avg(r.rate), 0) as decimal(53, 20))  as rate " +
            "              from store s " +
            "             inner join category c  " +
            "                     on c.id=s.category_id  " +
            "              left join orders o " +
            "                     on o.store_id=s.id  " +
            "                    and o.order_status = :orderStatus " +
            "              left join review r " +
            "                     on r.order_id=o.id  " +
            "              left join menu m " +
            "                     on m.store_id=s.id  " +
            "             where ( " +
            "                     s.store_status != 'SHUTDOWN' " +
            "                   )           " +
            "               and s.store_status = :storeStatus  " +
            "               and m.menu_name like concat(:keyword, '%')  " +
            "               and st_contains(st_buffer(:location, 4000), s.location)  " +
            "             group by s.id  " +
            "     ) total " +
            "group by total.id " +
            "order by total.rate " +
            "limit :offset, :limit ",
            nativeQuery = true)
    List<StoreResponseForNativeQuery> searchOrderByRate(Point location, String keyword, String orderStatus, String storeStatus, long offset, int limit);

}
