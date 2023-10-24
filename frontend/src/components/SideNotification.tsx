/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useRef, useState } from 'react'
import * as S from '../styles/ui/SideNotification.styled'

import NotificationItem from './notification/NotificationItem'
import axiosInstance from '../utils/FetchCall'
import { NotiItem } from '../constants/Interfaces'
import useIcon from '../hooks/useIcon'
import { palette } from '../constants/Styles'

const SideNotification = () => {
  const width = 280
  const { IcBell } = useIcon()
  const [isOpen, setIsOpen] = useState(false)
  const [xPosition, setXPosition] = useState(-width)
  const [notiList, setNotiList] = useState<NotiItem[]>([])

  const side = useRef<HTMLDivElement | null>(null)

  const toggleMenu = () => {
    if (xPosition < 0) {
      setXPosition(0)
      setIsOpen(true)
    } else {
      setXPosition(-width)
      setIsOpen(false)
    }
  }

  // 사이드바 외부 클릭시 닫히는 함수
  const handleClose = (e: MouseEvent) => {
    if (!side.current) return

    const sideArea = side.current
    const sideCildren = side.current.contains(e.target as Node)
    if (isOpen && (!sideArea || !sideCildren)) {
      setXPosition(-width)
      setIsOpen(false)
    }
  }

  const getNoti = async () => {
    const pageIdx = 0
    const pageSize = 20
    const res = await axiosInstance.get(
      `/api/notification/${pageIdx}/${pageSize}`
    )

    setNotiList(res.data.content)
  }

  useEffect(() => {
    window.addEventListener('click', handleClose)
    getNoti()
    return () => {
      window.removeEventListener('click', handleClose)
    }
  }, [])

  return (
    <S.SideBar
      ref={side}
      style={{
        width: `${width}px`,
        height: '100%',
        transform: `translatex(${-xPosition}px)`,
      }}
    >
      <S.Button onClick={toggleMenu}>
        <IcBell size={3} color={palette.textPrimary} />
        {/* {isOpen ? <span>1</span> : <span>1</span>} */}
      </S.Button>
      <S.Content>
        {notiList.length > 0 ? (
          notiList.map(notiItem => (
            <NotificationItem key={notiItem.id} item={notiItem} />
          ))
        ) : (
          <S.EmptyList>알림이 없습니다</S.EmptyList>
        )}
      </S.Content>
    </S.SideBar>
  )
}
export default SideNotification
