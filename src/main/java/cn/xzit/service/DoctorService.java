package cn.xzit.service;

import cn.xzit.entity.base.Medicinal;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DoctorService {

    List<Medicinal> getAllMedName();


    String addPrescript(String medNames, String nums, HttpServletRequest request);
}
