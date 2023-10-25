import { ChangeEvent, useEffect, useState } from 'react'
import { useRecoilState } from 'recoil'
import DaumPostcodeEmbed from 'react-daum-postcode'
import BottomNavigation from '../../components/BottomNavigation'
import Header from '../../components/Header'
import * as S from '../../styles/user/ProfileEdit.styled'
import axiosInstance, { putFormData } from '../../utils/FetchCall'
import Modal from '../../components/auth/AddressModal'
import {
  validateNickname,
  validatePhoneNumber,
} from '../../utils/FormValidation'
import profileDefault from '../../../public/images/default_profile.png'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'
import { myInfoState } from '../../store/recoilState'
import { useNavigate } from 'react-router-dom'

const ProfileEditPage = () => {
  const navigate = useNavigate()
  const [, setProfile] = useRecoilState(myInfoState)

  const [isChecked, setIsChecked] = useState(false)
  const [isPhoneChecked, setIsPhoneChecked] = useState(false)
  const [isAddressModal, setIsAddressModal] = useState(false)
  const [isAlertModal, setIsAlertModal] = useState(false)
  const [modalContent, setModalContent] = useState('')
  const [originNickName, setOriginNickName] = useState('')
  const [isEditAble, setIsEditAble] = useState(false)

  const [originPhone, setOriginPhone] = useState('')
  const [profileImage, setProfileImage] = useState<File>()
  const [profileValue, setProfileValue] = useState({
    nickName: '',
    addressDetail: '',
    roadAddress: '',
    phoneNumber: '',
    profileUrl: '',
  })

  const onCheckNickName = async () => {
    try {
      const res = await axiosInstance.get(`/api/auth/check/nickname`, {
        params: { nickname: profileValue.nickName },
      })
      if (res.status === 200) {
        setIsChecked(true)
      }
    } catch (error) {
      console.log(error)
      setModalContent('중복 된 닉네임입니다.')
      setIsAlertModal(true)
      setTimeout(() => {
        setIsAlertModal(false)
      }, 1500)
    }
  }

  const onCheckPhone = async () => {
    try {
      const res = await axiosInstance.get(`/api/auth/check/phone`, {
        params: { phoneNumber: profileValue.phoneNumber },
      })
      if (res.status === 200) {
        setIsPhoneChecked(true)
      }
      console.log(res)
    } catch (error) {
      setModalContent('중복 된 전화번호입니다.')
      setIsAlertModal(true)
      setTimeout(() => {
        setIsAlertModal(false)
      }, 1500)
    }
  }

  const onChangeValue = (e: React.ChangeEvent<HTMLInputElement>) => {
    setProfileValue({ ...profileValue, [e.target.name]: e.target.value })
  }

  const getProfile = async () => {
    const { data } = await axiosInstance.get('/api/profile/private')
    console.log(data)
    setOriginNickName(data.nickName)
    setOriginPhone(data.phoneNumber)
    setProfileValue({
      nickName: data.nickName,
      addressDetail: data.address.addressDetail,
      roadAddress: data.address.roadAddress,
      phoneNumber: data.phoneNumber,
      profileUrl: data.profileUrl,
    })
  }

  const setMyProfile = async () => {
    const response = await axiosInstance.get('/api/profile/private')
    console.log(response)
    if (response.status === 200) {
      setProfile(response.data)
      localStorage.setItem('PROFILE', JSON.stringify(response.data))
    }
  }

  const editProfile = async () => {
    if (!isEditAble) return
    const form = new FormData()
    form.append('nickName', profileValue.nickName)
    form.append('phoneNumber', profileValue.phoneNumber)
    form.append('roadAddress', profileValue.roadAddress)
    form.append('addressDetail', profileValue.addressDetail)
    if (profileImage) {
      form.append('profileImage', profileImage as Blob)
    }

    try {
      await putFormData('/api/profile/private', form)
      setMyProfile()
      setModalContent('프로필 수정이 완료되었습니다.')
    } catch (error) {
      console.log(error)
      setModalContent('프로필 수정에 실패하였습니다.')
    }
    setIsAlertModal(true)
    setTimeout(() => {
      setIsAlertModal(false)
      navigate(-1)
    }, 1500)
  }

  const uploadImage = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      const imageURL = URL.createObjectURL(file)
      setProfileImage(file)
      setProfileValue({ ...profileValue, profileUrl: imageURL })
    }
  }

  useEffect(() => {
    getProfile()
  }, [])

  useEffect(() => {
    if (
      (originNickName === profileValue.nickName || isChecked) &&
      (originPhone === profileValue.phoneNumber || isPhoneChecked) &&
      profileValue.roadAddress !== '' &&
      profileValue.addressDetail !== ''
    ) {
      setIsEditAble(true)
    } else {
      setIsEditAble(false)
    }
  }, [profileValue, isChecked, isPhoneChecked])

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapImage>
          {profileValue.profileUrl ? (
            <S.Image $size={20} src={profileValue.profileUrl} />
          ) : (
            <>
              <S.FileLabel htmlFor="profileImage">
                <S.Image $size={15} src={profileDefault} />
              </S.FileLabel>
              <S.File
                id="profileImage"
                type="file"
                accept="image/*"
                onChange={uploadImage}
              />
            </>
          )}
        </S.WrapImage>
        <S.WrapForm>
          <S.FlexWrap>
            <S.Label>닉네임</S.Label>
            <S.Flex>
              <S.Input
                name="nickName"
                value={profileValue.nickName}
                onChange={onChangeValue}
                $readOnly={isChecked}
                readOnly={isChecked}
              />
              {!isChecked && (
                <S.CheckNickNameButton
                  $isActive={
                    profileValue.nickName !== originNickName &&
                    validateNickname(profileValue.nickName)
                  }
                  onClick={() => {
                    if (
                      profileValue.nickName !== originNickName &&
                      validateNickname(profileValue.nickName)
                    ) {
                      onCheckNickName()
                    }
                  }}
                >
                  중복 확인
                </S.CheckNickNameButton>
              )}
            </S.Flex>
          </S.FlexWrap>
          <S.FlexWrap>
            <S.Label>핸드폰</S.Label>
            <S.Flex>
              <S.Input
                name="phoneNumber"
                value={profileValue.phoneNumber}
                onChange={onChangeValue}
                $readOnly={isPhoneChecked}
                readOnly={isPhoneChecked}
              />
              {!isPhoneChecked && (
                <S.CheckNickNameButton
                  $isActive={
                    profileValue.phoneNumber !== originPhone &&
                    validatePhoneNumber(profileValue.phoneNumber)
                  }
                  onClick={() => {
                    if (
                      profileValue.phoneNumber !== originPhone &&
                      validatePhoneNumber(profileValue.phoneNumber)
                    ) {
                      onCheckPhone()
                    }
                  }}
                >
                  중복 확인
                </S.CheckNickNameButton>
              )}
            </S.Flex>
          </S.FlexWrap>
          <S.FlexWrap>
            <S.Label>주소</S.Label>
            <S.Flex>
              <S.Input
                name="roadAddress"
                value={profileValue.roadAddress}
                onChange={onChangeValue}
                readOnly
              />
              <S.Button onClick={() => setIsAddressModal(true)}>
                주소 검색
              </S.Button>
            </S.Flex>
          </S.FlexWrap>
          <S.FlexWrap>
            <S.Label>상세 주소</S.Label>
            <S.Flex>
              <S.Input
                name="addressDetail"
                value={profileValue.addressDetail}
                onChange={onChangeValue}
              />
            </S.Flex>
          </S.FlexWrap>
          <S.EditButton $isActive={isEditAble} onClick={editProfile}>
            수정하기
          </S.EditButton>
        </S.WrapForm>
      </S.Container>
      <Modal
        isOpen={isAddressModal}
        onClose={() => setIsAddressModal(false)}
        title="주소 검색"
        content={
          <DaumPostcodeEmbed
            onComplete={data => {
              setProfileValue({ ...profileValue, roadAddress: data.address })
              setIsAddressModal(false)
            }}
            autoClose={false}
          />
        }
      />
      {isAlertModal && (
        <ModalWithoutButton
          content={modalContent}
          setIsModalFalse={() => setIsAlertModal(false)}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default ProfileEditPage
