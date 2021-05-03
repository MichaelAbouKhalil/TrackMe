package com.trackme.userservice.service;

import com.trackme.common.security.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.profile.ProfileRequest;
import com.trackme.models.profile.ProfileEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    /**
     * find profile by username
     *
     * @param username
     * @return
     */
    public Optional<ProfileEntity> findProfileByUsername(String username) {

        log.info("finding profile for user [{}]", username);
        Optional<ProfileEntity> optionalProfile = profileRepository.findById(username);

        if (!optionalProfile.isPresent()) {
            log.info("profile not found");
        }
        return optionalProfile;
    }

    /**
     * retrieve/build user profile
     *
     * @return
     */
    public CommonResponse getCompleteUserProfile() {

        UserEntity user = userService.getUser();
        if (user == null) {
            // this should never reach because user was already authenticated.
            throw new UsernameNotFoundException("user with username [" + user.getUsername() + "] is not found");
        }

        Optional<ProfileEntity> optionalProfile = findProfileByUsername(user.getUsername());

        ProfileEntity profile;
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else {
            profile = ProfileEntity.builder()
                    .username(user.getUsername())
                    .build();
        }
        profile.setUserEntity(user);

        CommonResponse response = CommonResponse.ok(profile);
        return response;
    }

    /**
     * save or update existing profile
     *
     * @param request
     * @return
     */
    public CommonResponse saveUpdateProfile(ProfileRequest request) {

        String authUsername = SecurityUtils.getUsername();

        Optional<ProfileEntity> optionalProfile = findProfileByUsername(authUsername);

        ProfileEntity profile;
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else {
            profile = ProfileEntity.builder()
                    .username(authUsername)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();
        }

        ProfileEntity savedProfile = profileRepository.save(profile);

        if(savedProfile == null){
            throw new RuntimeException("Failed to save user in database");
        }

        return CommonResponse.ok(savedProfile);
    }

}
