package com.vit_ana.scheduler.dao.user.provider;

import org.springframework.data.jpa.repository.Query;

import com.vit_ana.scheduler.dao.user.CommonUserRepository;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.user.provider.Provider;

import java.util.List;

public interface ProviderRepository extends CommonUserRepository<Provider> {

    List<Provider> findByWorks(Work work);

    @Query("select distinct p from Provider p inner join p.works w where w.targetCustomer = 'retail'")
    List<Provider> findAllWithRetailWorks();

    @Query("select distinct p from Provider p inner join p.works w where w.targetCustomer = 'corporate'")
    List<Provider> findAllWithCorporateWorks();
}
