/* eslint-disable react-hooks/exhaustive-deps */
import { useState } from 'react'
import { useRecoilValue } from 'recoil'
import * as S from '../../styles/refrigerator/RefrigeratorPage.styled'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import CookForMe from '../../components/refrigerator/CookForMe'
import CookForYou from '../../components/refrigerator/CookForYou'

import { myInfoState } from '../../store/recoilState'
import RefrigeratorMap from '../../components/refrigerator/RefrigeratorMap'
import { MarkerItem } from '../../constants/Interfaces'

const RefrigeratorPage = () => {
  const profile = useRecoilValue(myInfoState)
  const [userType, setUserType] = useState('user')
  const [mapMarkerList, setMapMarkerList] = useState<MarkerItem[]>([])

  return (
    <>
      <Header />
      <S.Container>
        <RefrigeratorMap markerList={mapMarkerList} />
        {profile?.isChef && (
          <S.SelectUserList>
            <S.SelectTypeButton
              type="button"
              onClick={() => setUserType('user')}
              $isActive={userType === 'user'}
            >
              요청자
            </S.SelectTypeButton>
            <S.SelectTypeButton
              type="button"
              onClick={() => setUserType('chef')}
              $isActive={userType === 'chef'}
            >
              요리사
            </S.SelectTypeButton>
          </S.SelectUserList>
        )}

        {userType === 'user' ? (
          <CookForMe setMapMarkerList={setMapMarkerList} />
        ) : (
          <CookForYou setMapMarkerList={setMapMarkerList} />
        )}
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default RefrigeratorPage
