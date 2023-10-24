/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/refrigerator/CookForYou.styled'
import axiosInstance from '../../utils/FetchCall'
import CustomSelect from '../ui/CustomSelect'
import { Requester } from '../../constants/Interfaces'
import EmptyView from '../ui/EmptyView'
import { REQUEST_OPTION_MENU_LIST } from '../../constants/Data'
import defaultProfile from '../../../public/images/default_profile.png'

const CookForYou = ({
  setMapMarkerList,
}: {
  setMapMarkerList: (e: any) => void
}) => {
  const navigate = useNavigate()

  const [list, setList] = useState<Requester[]>([])
  const [currentSelectValue, setCurrentSelectValue] = useState(
    REQUEST_OPTION_MENU_LIST[0]
  )

  // 요청서 detail page로 이동
  const goToRequestDetail = (id: number) => {
    navigate(`/refrigerator/request/detail/${id}`)
  }

  // 주위 요청서 조회 API
  const getRequest = async () => {
    const pageIdx = 0
    const pageSize = 10
    const res = await axiosInstance.get(
      `/api/refrigerator/requester/${pageIdx}/${pageSize}`,
      { params: { type: currentSelectValue.value } }
    )
    console.log(res)
    setList(res.data.content)
    setMapMarkerList(res.data.content)
  }

  useEffect(() => {
    getRequest()
  }, [currentSelectValue])

  return (
    <>
      <S.ChefListContainer>
        <S.Info>
          <S.SubTitle>내 주변 요청</S.SubTitle>
          <CustomSelect
            data={REQUEST_OPTION_MENU_LIST}
            currentSelectValue={currentSelectValue}
            setCurrentSelectValue={setCurrentSelectValue}
          />
        </S.Info>
        <S.CardList>
          {list.length > 0 ? (
            list.map(item => (
              <S.Card key={item.memberId}>
                <S.CardInfo className="card-info">
                  <S.MainImg
                    src={defaultProfile}
                    alt="대표 사진"
                    className="mainImg"
                  />
                  <S.ChefInfo className="chef-info">
                    <S.ChefTopInfo className="top-info">
                      <span className="nickName">{item.nickname}</span>
                    </S.ChefTopInfo>
                    <S.ChefBottomInfo className="bottom-info">
                      {/* {item.description} */}
                    </S.ChefBottomInfo>
                  </S.ChefInfo>
                </S.CardInfo>
                <S.ElseInfo>
                  <span>{item.distance}m</span>
                  <S.RequestButton
                    type="button"
                    onClick={() => goToRequestDetail(item.requestId)}
                  >
                    요청서 보기
                  </S.RequestButton>
                </S.ElseInfo>
              </S.Card>
            ))
          ) : (
            <EmptyView content="요청서가 존재하지 않습니다." />
          )}
        </S.CardList>
      </S.ChefListContainer>
      <S.SpaceDiv />
    </>
  )
}

export default CookForYou
