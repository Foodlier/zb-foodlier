import React from 'react'
import { useRecoilState } from 'recoil'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/refrigerator/ChefCard.styled'
import useIcon from '../../hooks/useIcon'
import { Chef } from '../../constants/Interfaces'
import { requireChefIdState } from '../../store/recoilState'
import defaultProfile from '../../../public/images/default_profile.png'
import axiosInstance from '../../utils/FetchCall'

interface ChefCardProps {
  chefItem: Chef
  isRequest: boolean
  setIsRequestModal: (value: boolean) => void
  refresh: () => void
}

const ChefCard: React.FC<ChefCardProps> = ({
  chefItem,
  isRequest,
  setIsRequestModal,
  refresh,
}) => {
  const navigate = useNavigate()
  const { IcStar } = useIcon()

  const [, setRequireChefId] = useRecoilState(requireChefIdState)

  const cancelRequest = async () => {
    const response = await axiosInstance.patch(
      `/api/refrigerator/cancel/${chefItem.requestId}`
    )
    refresh()
    console.log(response)
  }

  return (
    <S.Card>
      <S.CardInfo className="card-info">
        <img
          src={chefItem.profileUrl || defaultProfile}
          alt="프로필 사진"
          className="mainImg"
        />
        <S.ChefInfo className="chef-info">
          <S.ChefTopInfo className="top-info">
            <span className="nickName">{chefItem.nickname}</span>
            <IcStar size={1.8} />
            <span className="rating">{`${chefItem.starAvg}(${chefItem.reviewCount})`}</span>
          </S.ChefTopInfo>
          <S.ChefBottomInfo className="bottom-info">
            {chefItem.introduce}
          </S.ChefBottomInfo>
        </S.ChefInfo>
      </S.CardInfo>
      <S.ElseInfo>
        <span>{chefItem.distance}m</span>
        {isRequest ? (
          <S.FlexWrap>
            {chefItem?.isQuotation && (
              <S.RequestedButton
                type="button"
                onClick={() =>
                  navigate(
                    `/refrigerator/quotation/detail/${chefItem.quotationId}`,
                    {
                      state: {
                        requestId: chefItem?.requestId,
                        chef: chefItem?.nickname,
                      },
                    }
                  )
                }
                $isActive
              >
                견적서 보러가기
              </S.RequestedButton>
            )}
            <S.RequestedButton
              onClick={cancelRequest}
              type="button"
              $isActive={false}
            >
              요청 취소
            </S.RequestedButton>
          </S.FlexWrap>
        ) : (
          <S.RequestButton
            type="button"
            onClick={() => {
              setRequireChefId(chefItem.chefId)
              setIsRequestModal(true)
            }}
          >
            요청하기
          </S.RequestButton>
        )}
      </S.ElseInfo>
    </S.Card>
  )
}

export default ChefCard
