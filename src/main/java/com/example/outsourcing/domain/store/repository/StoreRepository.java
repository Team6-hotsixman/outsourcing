package com.example.outsourcing.domain.store.repository;

import com.example.outsourcing.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT COUNT(s) " +
            "FROM Store s " +
            "WHERE s.user.id = :userId")
    long countStoresByUserId(Long userId);

    @Query(value = "select s.* " +
            "         from store s " +
            "         left join menu m " +
            "                on m.store_id = s.id " +
            "         left join category sc " +
            "                on s.category_id = sc.id " +
            "         left join category mc " +
            "                on m.category_id = mc.id " +
            "        where 1 = 1" +
            "          and ST_Distance_Sphere(s.location, ST_GEOMFROMTEXT(concat('POINT(', :longitude, ' ', :latitude, ')'))) <= :radius " +
            "          and ( :keyword is null " +
            "                or sc.category like concat('%', :keyword,'%') " +
            "                or mc.category like concat('%', :keyword,'%') " +
            "                or m.menu_name like concat('%', :keyword,'%') " +
            "                or s.store_name like concat('%', :keyword,'%') " +
            "              ) " +
            "        group by s.id ",
            nativeQuery = true)
    List<Store> findStoresByWithinRadius(@Param("longitude") double longitude,
                                         @Param("latitude") double latitude,
                                         @Param("radius") double radius,
                                         @Param("keyword") String keyword);


}
