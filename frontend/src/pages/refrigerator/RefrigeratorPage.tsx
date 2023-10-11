import { useEffect, useState } from 'react'
import { Map } from 'react-kakao-maps-sdk'
import { useRecoilValue } from 'recoil'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/RefrigeratorPage.styled'
import CookForMe from '../../components/refrigerator/CookForMe'
import CookForYou from '../../components/refrigerator/CookForYou'
import { myInfoState } from '../../store/recoilState'
import axiosInstance from '../../utils/FetchCall'

const RefrigeratorPage = () => {
  const profile = useRecoilValue(myInfoState)
  const [userType, setUserType] = useState('requester')

  const pageIdx = 1
  const pageSize = 20

  const getChef = async () => {
    try {
      const res = await axiosInstance.get(
        `/refrigerator/chef/${pageIdx}/${pageSize}`,
        { params: { type: 'DISTANCE' } }
      )
      console.log(res)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getChef()
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.Map>
          <Map
            center={{ lat: profile.address.lat, lng: profile.address.lnt }} // 지도의 중심 좌표
            style={{ width: '100%', height: '100%' }} // 지도 크기
            level={4}
          />
        </S.Map>
        <S.SelectUserList>
          <S.SelectTypeButton
            type="button"
            onClick={() => setUserType('requester')}
          >
            요청자
          </S.SelectTypeButton>
          <S.SelectTypeButton
            type="button"
            onClick={() => setUserType('cooker')}
          >
            요리사
          </S.SelectTypeButton>
        </S.SelectUserList>
        {userType === 'requester' ? <CookForMe /> : <CookForYou />}
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default RefrigeratorPage
