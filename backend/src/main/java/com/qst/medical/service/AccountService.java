package com.qst.medical.service;

import com.qst.medical.entity.Account;
import com.qst.medical.entity.MyUserDetails;
import com.qst.medical.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account login(String uname, String pwd) {
        Account account = accountMapper.selectByUname(uname);
        if (account != null && passwordEncoder.matches(pwd, account.getPwd())) {
            return account;
        }
        return null;
    }

    public void register(Account account) {
        account.setPwd(passwordEncoder.encode(account.getPwd()));
        accountMapper.insert(account);
    }

    public Account getById(Long id) {
        return accountMapper.selectById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
        Account account = accountMapper.selectByUname(uname);
        if (account == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new MyUserDetails(account);
    }
}