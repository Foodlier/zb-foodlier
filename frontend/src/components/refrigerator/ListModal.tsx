/* eslint-disable react/no-array-index-key */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react/require-default-props */
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRecoilState } from 'recoil'
import * as S from '../../styles/refrigerator/ListModal.styled'
import axiosInstance from '../../utils/FetchCall'
import { Quotation, RequestForm } from '../../constants/Interfaces'
import { requireChefIdState } from '../../store/recoilState'
import useIcon from '../../hooks/useIcon'
import QuotationItem from './QuotationItem'
import { palette } from '../../constants/Styles'
import RequestItem from './RequestItem'

interface ModalProps {
  setModalOpen: React.Dispatch<React.SetStateAction<boolean>>
  modalType: 'request' | 'quotation'
  setIsSelectedRequestId?: (id: number) => void
  postRequest?: (id: number) => void
}

const ListModal: React.FC<ModalProps> = ({
  setModalOpen,
  modalType,
  setIsSelectedRequestId,
  postRequest,
}) => {
  const { IcAddRoundDuotone, IcCloseRound } = useIcon()
  const navigate = useNavigate()

  const MODAL_OPTION = {
    type: modalType === 'quotation' ? '견적서' : '요청서',
  }

  const [requireChefId] = useRecoilState(requireChefIdState)

  const [list, setList] = useState<RequestForm[] | Quotation[]>([])
  const [isRefresh, setIsRefresh] = useState(false)

  // 요청서 목록 조회 API
  const getRequireList = async () => {
    const pageIdx = 0
    const pageSize = 10

    try {
      const res = await axiosInstance.get(
        `/api/refrigerator/${pageIdx}/${pageSize}`
      )
      console.log(res)
      setList(res.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  // 견적서 목록 조회 API
  const getQuotateList = async () => {
    const pageIdx = 0
    const pageSize = 10

    try {
      const res = await axiosInstance.get(
        `/api/quotation/${pageIdx}/${pageSize}`
      )
      console.log(res)
      setList(res.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  const onClickSelectButton = (id: number) => {
    if (setIsSelectedRequestId) {
      setIsSelectedRequestId(id)
      setModalOpen(false)
    }
  }

  const goToAddList = () => {
    if (modalType === 'quotation') {
      navigate('/refrigerator/quotation/write')
    } else {
      navigate('/refrigerator/request/write')
    }
  }

  const refresh = () => {
    setIsRefresh(!isRefresh)
  }

  useEffect(() => {
    if (modalType === 'quotation') {
      getQuotateList()
    } else {
      getRequireList()
    }
  }, [isRefresh])

  return (
    <>
      <S.GreyBackground />
      <S.ModalContainer>
        <S.ModalHeader>
          <S.ModalTitle> {`나의 ${MODAL_OPTION.type} 목록`}</S.ModalTitle>
          <S.CloseButton type="button" onClick={() => setModalOpen(false)}>
            <IcCloseRound size={3} color={palette.textPrimary} />
          </S.CloseButton>
        </S.ModalHeader>
        <S.ElContainer>
          {list.length > 0 ? (
            list.map((item, index) => (
              <React.Fragment key={`key-${index}`}>
                {modalType === 'quotation' ? (
                  <QuotationItem
                    item={item}
                    onClickSelectButton={onClickSelectButton}
                    refresh={refresh}
                  />
                ) : (
                  <RequestItem
                    item={item}
                    postRequest={postRequest}
                    isVisibleSelectButton={Boolean(requireChefId)}
                    refresh={refresh}
                  />
                )}
              </React.Fragment>
            ))
          ) : (
            <S.EmptyView>{`${MODAL_OPTION.type}가 존재하지 않습니다.`}</S.EmptyView>
          )}
          <S.AddListButton onClick={goToAddList}>
            <IcAddRoundDuotone size={5} />
          </S.AddListButton>
        </S.ElContainer>
      </S.ModalContainer>
    </>
  )
}

export default ListModal
