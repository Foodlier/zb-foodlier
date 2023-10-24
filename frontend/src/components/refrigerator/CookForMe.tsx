/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRecoilState } from 'recoil'
import ListModal from './ListModal'
import * as S from '../../styles/refrigerator/CookForMe.styled'
import ChefCard from './ChefCard'
import { Chef } from '../../constants/Interfaces'
import { OPTION_MENU_LIST } from '../../constants/Data'
import CustomSelect from '../ui/CustomSelect'
import { requireChefIdState } from '../../store/recoilState'
import EmptyView from '../ui/EmptyView'
import axiosInstance from '../../utils/FetchCall'

const CookForMe = ({
  setMapMarkerList,
}: {
  setMapMarkerList: (e: any) => void
}) => {
  const navigate = useNavigate()

  const [requireChefId, setRequireChefId] = useRecoilState(requireChefIdState)
  const [isRequestModal, setIsRequestModal] = useState(false)
  const [isRefresh, setIsRefresh] = useState(false)
  const [chefList, setChefList] = useState<Chef[]>([])
  const [requestedChefList, setRequestedChefList] = useState<Chef[]>([])
  const [currentSelectValue, setCurrentSelectValue] = useState(
    OPTION_MENU_LIST[0]
  )

  const showRequest = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault()
    setRequireChefId(0)
    setIsRequestModal(true)
  }

  const pageIdx = 0
  const pageSize = 20

  const refresh = () => {
    setIsRefresh(!isRefresh)
  }

  // 요리사에게 요청서 전송 API
  const postRequest = async (requestFormId: number) => {
    try {
      const res = await axiosInstance.patch(
        `/api/refrigerator/send?chefMemberId=${requireChefId}&requestFormId=${requestFormId}`
      )
      console.log(res)
    } catch (error) {
      console.log(error)
    }
    setIsRequestModal(false)
    refresh()
  }

  const getChef = async () => {
    try {
      const chefList = await axiosInstance.get(
        `/api/refrigerator/chef/${pageIdx}/${pageSize}`,
        { params: { type: currentSelectValue.value } }
      )

      const requestdChefList = await axiosInstance.get(
        `/api/refrigerator/chef/requested/${pageIdx}/${pageSize}`,
        { params: { type: currentSelectValue.value } }
      )
      setChefList(chefList.data.content)
      setRequestedChefList(requestdChefList.data.content)
      setMapMarkerList(chefList.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getChef()
  }, [currentSelectValue, isRefresh])

  return (
    <>
      <S.Container>
        <S.Info>
          <S.SubTitle>내 주변 요리사</S.SubTitle>
          <CustomSelect
            data={OPTION_MENU_LIST}
            currentSelectValue={currentSelectValue}
            setCurrentSelectValue={setCurrentSelectValue}
          />
        </S.Info>
        <S.CardList>
          {requestedChefList.length + chefList.length > 0 ? (
            <>
              {requestedChefList.map(item => (
                <ChefCard
                  key={item.chefId}
                  chefItem={item}
                  setIsRequestModal={setIsRequestModal}
                  refresh={refresh}
                  isRequest
                />
              ))}
              {chefList.map(item => (
                <ChefCard
                  key={item.chefId}
                  chefItem={item}
                  setIsRequestModal={setIsRequestModal}
                  refresh={refresh}
                  isRequest={false}
                />
              ))}
            </>
          ) : (
            <EmptyView content="주위에 요리사가 없습니다." />
          )}
        </S.CardList>
        <S.ButtonList>
          <S.WritingButton
            type="button"
            onClick={() => navigate('/refrigerator/request/write')}
          >
            + 요청서 작성하기
          </S.WritingButton>
          <S.WritingButton type="button" onClick={showRequest}>
            요청서 목록
          </S.WritingButton>
        </S.ButtonList>
      </S.Container>

      {isRequestModal && (
        <ListModal
          setModalOpen={setIsRequestModal}
          modalType="request"
          postRequest={postRequest}
        />
      )}
    </>
  )
}

export default CookForMe
